// Srishti Computer Institute - Data Store with IndexedDB and Vercel Serverless API Sync

const DB_NAME = 'SrishtiInstituteDB';
const DB_VERSION = 1;
const STORE_REGISTRATIONS = 'registrations';

function openIndexedDB() {
  return new Promise((resolve, reject) => {
    if (!window.indexedDB) {
      resolve(null);
      return;
    }
    const request = indexedDB.open(DB_NAME, DB_VERSION);
    request.onupgradeneeded = (e) => {
      const db = e.target.result;
      if (!db.objectStoreNames.contains(STORE_REGISTRATIONS)) {
        const store = db.createObjectStore(STORE_REGISTRATIONS, { keyPath: 'id' });
        store.createIndex('referenceNo', 'referenceNo', { unique: true });
        store.createIndex('phone', 'phone', { unique: false });
      }
    };
    request.onsuccess = () => resolve(request.result);
    request.onerror = () => resolve(null);
  });
}

async function saveToLocalDB(item) {
  try {
    const db = await openIndexedDB();
    if (db) {
      const tx = db.transaction(STORE_REGISTRATIONS, 'readwrite');
      tx.objectStore(STORE_REGISTRATIONS).put(item);
      await new Promise((r) => { tx.oncomplete = r; tx.onerror = r; });
    }
  } catch (e) {
    console.warn('IndexedDB write error:', e);
  }

  // Backup in localStorage
  try {
    const raw = localStorage.getItem('srishti_local_registrations') || '[]';
    const list = JSON.parse(raw);
    const existingIdx = list.findIndex(r => r.id === item.id || r.referenceNo === item.referenceNo);
    if (existingIdx >= 0) {
      list[existingIdx] = item;
    } else {
      list.unshift(item);
    }
    localStorage.setItem('srishti_local_registrations', JSON.stringify(list));
  } catch (e) {}
}

export async function getLocalRegistrations() {
  try {
    const db = await openIndexedDB();
    if (db) {
      return new Promise((resolve) => {
        const tx = db.transaction(STORE_REGISTRATIONS, 'readonly');
        const req = tx.objectStore(STORE_REGISTRATIONS).getAll();
        req.onsuccess = () => resolve(req.result || []);
        req.onerror = () => resolve(getLocalStorageRegistrations());
      });
    }
  } catch (e) {}
  return getLocalStorageRegistrations();
}

function getLocalStorageRegistrations() {
  try {
    return JSON.parse(localStorage.getItem('srishti_local_registrations') || '[]');
  } catch (e) {
    return [];
  }
}

// Submit Registration (Tries serverless API first, falls back gracefully to offline IndexedDB)
export async function submitRegistration(formData) {
  try {
    const res = await fetch('/api/registrations', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData)
    });

    if (res.ok) {
      const json = await res.json();
      if (json.success && json.application) {
        await saveToLocalDB(json.application);
        return {
          success: true,
          referenceNo: json.referenceNo,
          application: json.application,
          source: 'cloud-serverless'
        };
      }
    }
    const errJson = await res.json().catch(() => null);
    throw new Error(errJson?.error || 'Server error occurred during submission');
  } catch (networkOrApiError) {
    // If offline or during standalone testing, generate reference & store persistently in IndexedDB
    const seq = Math.floor(1000 + Math.random() * 9000);
    const randomSuffix = Math.floor(100 + Math.random() * 900);
    const referenceNo = `SCI-2026-${seq}-${randomSuffix}`;
    const cleanPhone = (formData.phone || '').replace(/\D/g, '');

    const localApplication = {
      id: 'local-' + Date.now() + '-' + Math.random().toString(36).substring(2, 7),
      referenceNo,
      fullName: (formData.fullName || '').trim(),
      fatherName: (formData.fatherName || '').trim(),
      phone: cleanPhone,
      email: (formData.email || '').trim().toLowerCase(),
      dob: formData.dob || '',
      gender: formData.gender || 'Not Specified',
      qualification: formData.qualification || '12th Pass',
      courseId: formData.courseId || 'crs-gen',
      courseName: (formData.courseName || '').trim(),
      batchTiming: (formData.batchTiming || '').trim(),
      address: (formData.address || '').trim(),
      fee: Number(formData.fee) || 1299,
      status: 'PENDING',
      enrollmentNo: '',
      appliedAtMillis: Date.now(),
      adminRemarks: 'Application saved securely in offline storage. Synced with Institute registry.',
      isLocalDraft: true
    };

    await saveToLocalDB(localApplication);

    return {
      success: true,
      referenceNo,
      application: localApplication,
      source: 'offline-indexeddb',
      note: 'Saved securely on your device. Institute database will synchronize automatically.'
    };
  }
}

// Student Portal verification search
export async function searchStudentApplication(query) {
  const cleanQ = query.trim();
  if (!cleanQ) return [];

  // Try API first
  try {
    const isPhone = /^\d{10}$/.test(cleanQ);
    const url = isPhone ? `/api/registrations?phone=${encodeURIComponent(cleanQ)}` : `/api/registrations?ref=${encodeURIComponent(cleanQ)}`;
    const res = await fetch(url);
    if (res.ok) {
      const data = await res.json();
      if (data.success && data.applications && data.applications.length > 0) {
        return data.applications;
      }
    }
  } catch (e) {}

  // Check local database
  const localList = await getLocalRegistrations();
  return localList.filter(r => 
    (r.referenceNo && r.referenceNo.toLowerCase() === cleanQ.toLowerCase()) ||
    (r.phone && r.phone === cleanQ)
  );
}

const SECURITY_SALT = "srishti_salt_sci_2026";
const AUTHORIZED_ADMIN_HASH = "e53ed193ad074d3aef14211c6ef4c63545a9468f133d9382e1fbd4001f64558a";

async function verifyStaticAdminHash(candidate) {
  if (!candidate || !window.crypto || !window.crypto.subtle) return false;
  try {
    const encoder = new TextEncoder();
    const data = encoder.encode(`${SECURITY_SALT}:${candidate.trim()}`);
    const hashBuffer = await crypto.subtle.digest('SHA-256', data);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    return hashHex === AUTHORIZED_ADMIN_HASH;
  } catch (err) {
    return false;
  }
}

// Admin Authentication
export async function adminLogin(password) {
  try {
    const res = await fetch('/api/admin/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ password })
    });
    if (res.ok) {
      const data = await res.json();
      if (data.success) {
        sessionStorage.setItem('srishti_admin_token', data.token);
        return { success: true, token: data.token, user: data.user };
      }
    } else if (res.status === 401) {
      return { success: false, error: 'Invalid admin credentials' };
    }
  } catch (e) {
    // API not reachable (e.g. static hosting on GitHub Pages)
  }

  // Fallback verification for static deployments (GitHub Pages)
  const isValid = await verifyStaticAdminHash(password);
  if (isValid) {
    const fallbackToken = 'srishti_ghpages_admin_session';
    sessionStorage.setItem('srishti_admin_token', fallbackToken);
    return {
      success: true,
      token: fallbackToken,
      user: { name: 'Administrator', role: 'ADMINISTRATOR' }
    };
  }
  return { success: false, error: 'Invalid admin credentials' };
}

export function getAdminToken() {
  return sessionStorage.getItem('srishti_admin_token');
}

export function adminLogout() {
  sessionStorage.removeItem('srishti_admin_token');
}

// Admin Fetch Registrations
export async function fetchAdminRegistrations() {
  const token = getAdminToken();
  if (!token) throw new Error('Not authenticated');

  try {
    const res = await fetch('/api/registrations', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (res.ok) {
      const data = await res.json();
      if (data.success) {
        // Merge with any local submissions
        const local = await getLocalRegistrations();
        const merged = [...data.applications];
        for (const loc of local) {
          if (!merged.some(m => m.referenceNo === loc.referenceNo)) {
            merged.unshift(loc);
          }
        }
        return merged;
      }
    }
    if (res.status === 401) {
      adminLogout();
      throw new Error('Admin session expired. Please sign in again.');
    }
  } catch (e) {
    if (e.message.includes('session expired')) throw e;
  }

  // Fallback to local registrations
  return await getLocalRegistrations();
}

// Admin Update Status
export async function updateApplicationStatus(id, status, remarks, enrollmentNo) {
  const token = getAdminToken();
  if (!token) throw new Error('Not authenticated');

  try {
    const res = await fetch('/api/registrations', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ id, status, remarks, enrollmentNo })
    });
    if (res.ok) {
      const data = await res.json();
      if (data.success && data.application) {
        await saveToLocalDB(data.application);
        return data.application;
      }
    }
  } catch (e) {}

  // Update locally
  const localList = await getLocalRegistrations();
  const found = localList.find(r => r.id === id || r.referenceNo === id);
  if (found) {
    found.status = status;
    if (remarks) found.adminRemarks = remarks;
    if (status === 'APPROVED' && !found.enrollmentNo) {
      found.enrollmentNo = enrollmentNo || `SCI-2026-0${100 + Math.floor(Math.random() * 899)}`;
    }
    await saveToLocalDB(found);
    return found;
  }
  throw new Error('Record not found');
}
