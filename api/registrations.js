import crypto from 'crypto';

// In-memory persistent cache for serverless environment
let REGISTRATIONS_CACHE = [
  {
    id: 'app-init-01',
    referenceNo: 'SCI-2026-1001',
    fullName: 'Rahul Verma',
    fatherName: 'Suresh Verma',
    phone: '9876543210',
    email: 'rahul.verma@example.com',
    dob: '2004-05-15',
    gender: 'Male',
    qualification: '12th Pass',
    courseId: 'crs-ai03',
    courseName: 'ADCA (Advance Diploma in Computer Application)',
    batchTiming: 'Morning (08:00 AM - 10:00 AM)',
    address: 'Mandhana, Kanpur Nagar, UP',
    fee: 1299,
    status: 'APPROVED',
    enrollmentNo: 'SCI-2026-0101',
    appliedAtMillis: Date.now() - 86400000 * 3,
    adminRemarks: 'Documents verified. Enrolled.'
  },
  {
    id: 'app-init-02',
    referenceNo: 'SCI-2026-1002',
    fullName: 'Pooja Sharma',
    fatherName: 'Ramesh Sharma',
    phone: '8765432109',
    email: 'pooja.sharma@example.com',
    dob: '2003-09-20',
    gender: 'Female',
    qualification: 'Graduate',
    courseId: 'crs-ai08',
    courseName: 'Certificate in TALLY Prime & GST',
    batchTiming: 'Mid-day (11:00 AM - 01:00 PM)',
    address: 'Bagdaudhi Kachhar, Kanpur Nagar, UP',
    fee: 1299,
    status: 'PENDING',
    enrollmentNo: '',
    appliedAtMillis: Date.now() - 86400000,
    adminRemarks: 'Pending original document review'
  }
];

function verifyAdminToken(authHeader) {
  if (!authHeader || !authHeader.startsWith('Bearer ')) return false;
  const token = authHeader.substring(7);
  const parts = token.split('.');
  if (parts.length !== 2) return false;
  
  try {
    const payloadStr = Buffer.from(parts[0], 'base64url').toString('utf-8');
    const payload = JSON.parse(payloadStr);
    if (!payload.exp || payload.exp < Date.now()) return false;
    
    const tokenSecret = process.env.JWT_SECRET || 'srishti-secret-token-key-2026';
    const expectedHmac = crypto.createHmac('sha256', tokenSecret).update(payloadStr).digest('hex');
    return expectedHmac === parts[1];
  } catch (e) {
    return false;
  }
}

export default async function handler(req, res) {
  res.setHeader('Access-Control-Allow-Credentials', 'true');
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET,POST,PUT,PATCH,DELETE,OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  // GET: Fetch registrations (Admin or specific student inquiry)
  if (req.method === 'GET') {
    const { ref, phone } = req.query;

    // Student specific inquiry (publicly accessible)
    if (ref || phone) {
      const found = REGISTRATIONS_CACHE.filter(r => {
        if (ref && r.referenceNo.toLowerCase() === ref.trim().toLowerCase()) return true;
        if (phone && r.phone === phone.trim()) return true;
        return false;
      });
      return res.status(200).json({ success: true, applications: found });
    }

    // Admin access requires valid token
    const isAdmin = verifyAdminToken(req.headers.authorization);
    if (!isAdmin) {
      return res.status(401).json({ success: false, error: 'Unauthorized: Admin authentication token required' });
    }

    return res.status(200).json({
      success: true,
      count: REGISTRATIONS_CACHE.length,
      applications: REGISTRATIONS_CACHE
    });
  }

  // POST: Submit new student registration
  if (req.method === 'POST') {
    try {
      const data = req.body || {};
      const { fullName, phone, courseName, batchTiming, address } = data;

      // Validation
      if (!fullName || fullName.trim().length < 3) {
        return res.status(400).json({ success: false, error: 'Full name is required (min 3 characters)' });
      }
      const cleanPhone = (phone || '').replace(/\D/g, '');
      if (cleanPhone.length !== 10) {
        return res.status(400).json({ success: false, error: 'Valid 10-digit mobile number is required' });
      }
      if (!courseName) {
        return res.status(400).json({ success: false, error: 'Course selection is required' });
      }
      if (!batchTiming) {
        return res.status(400).json({ success: false, error: 'Batch timing preference is required' });
      }
      if (!address || address.trim().length < 5) {
        return res.status(400).json({ success: false, error: 'Full residential address is required' });
      }

      const seq = 1000 + REGISTRATIONS_CACHE.length + 1;
      const randomSuffix = Math.floor(100 + Math.random() * 900);
      const referenceNo = `SCI-2026-${seq}-${randomSuffix}`;
      const newRegistration = {
        id: 'reg-' + Date.now() + '-' + Math.random().toString(36).substring(2, 7),
        referenceNo,
        fullName: fullName.trim(),
        fatherName: (data.fatherName || '').trim(),
        phone: cleanPhone,
        email: (data.email || '').trim().toLowerCase(),
        dob: data.dob || '',
        gender: data.gender || 'Not Specified',
        qualification: data.qualification || '12th Pass',
        courseId: data.courseId || 'crs-gen',
        courseName: courseName.trim(),
        batchTiming: batchTiming.trim(),
        address: address.trim(),
        fee: Number(data.fee) || 1299,
        status: 'PENDING',
        enrollmentNo: '',
        appliedAtMillis: Date.now(),
        adminRemarks: 'Application submitted successfully. Awaiting verification.'
      };

      REGISTRATIONS_CACHE.unshift(newRegistration);

      return res.status(201).json({
        success: true,
        message: 'Student application submitted and saved successfully!',
        referenceNo,
        application: newRegistration
      });
    } catch (err) {
      return res.status(500).json({ success: false, error: err.message });
    }
  }

  // PUT / PATCH: Admin updates application status
  if (req.method === 'PUT' || req.method === 'PATCH') {
    const isAdmin = verifyAdminToken(req.headers.authorization);
    if (!isAdmin) {
      return res.status(401).json({ success: false, error: 'Unauthorized: Admin authentication token required' });
    }

    const { id, status, remarks, enrollmentNo } = req.body || {};
    const item = REGISTRATIONS_CACHE.find(r => r.id === id || r.referenceNo === id);
    if (!item) {
      return res.status(404).json({ success: false, error: 'Application record not found' });
    }

    if (status) item.status = status;
    if (remarks !== undefined) item.adminRemarks = remarks;
    if (status === 'APPROVED' && !item.enrollmentNo) {
      const approvedCount = REGISTRATIONS_CACHE.filter(r => r.enrollmentNo).length;
      item.enrollmentNo = enrollmentNo || `SCI-2026-0${100 + approvedCount + 1}`;
    }

    return res.status(200).json({
      success: true,
      message: 'Application updated successfully',
      application: item
    });
  }

  // DELETE: Remove application (Admin only)
  if (req.method === 'DELETE') {
    const isAdmin = verifyAdminToken(req.headers.authorization);
    if (!isAdmin) {
      return res.status(401).json({ success: false, error: 'Unauthorized: Admin token required' });
    }

    const { id } = req.query;
    const initialLen = REGISTRATIONS_CACHE.length;
    REGISTRATIONS_CACHE = REGISTRATIONS_CACHE.filter(r => r.id !== id && r.referenceNo !== id);
    return res.status(200).json({
      success: true,
      deleted: REGISTRATIONS_CACHE.length < initialLen
    });
  }

  return res.status(405).json({ success: false, error: 'Method not allowed' });
}
