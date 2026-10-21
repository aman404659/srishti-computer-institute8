import crypto from 'crypto';

// Salted hash for secure default admin authentication
// Password is never exposed in client code
const DEFAULT_ADMIN_HASH = 'e53ed193ad074d3aef14211c6ef4c63545a9468f133d9382e1fbd4001f64558a';
const SECURITY_SALT = 'srishti_salt_sci_2026';

function verifyPassword(candidate) {
  if (!candidate || typeof candidate !== 'string') return false;
  
  // If custom environment variable is set on Vercel, check that first
  if (process.env.ADMIN_PASSWORD) {
    return candidate.trim() === process.env.ADMIN_PASSWORD.trim();
  }

  // Fallback to cryptographic salted hash verification
  const md = crypto.createHash('sha256');
  md.update(`${SECURITY_SALT}:${candidate.trim()}`);
  const computedHash = md.digest('hex');
  return computedHash === DEFAULT_ADMIN_HASH;
}

export default async function handler(req, res) {
  // Set CORS headers
  res.setHeader('Access-Control-Allow-Credentials', 'true');
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'POST,OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  if (req.method !== 'POST') {
    return res.status(405).json({ success: false, error: 'Method not allowed' });
  }

  try {
    const { username, password } = req.body || {};

    if (!password) {
      return res.status(400).json({ success: false, error: 'Password is required' });
    }

    const isValid = verifyPassword(password);
    if (!isValid) {
      return res.status(401).json({ success: false, error: 'Invalid admin credentials' });
    }

    // Generate secure HMAC session token
    const tokenSecret = process.env.JWT_SECRET || 'srishti-secret-token-key-2026';
    const payload = JSON.stringify({
      user: username || 'Admin',
      role: 'ADMINISTRATOR',
      exp: Date.now() + 24 * 60 * 60 * 1000
    });
    const hmac = crypto.createHmac('sha256', tokenSecret).update(payload).digest('hex');
    const token = `${Buffer.from(payload).toString('base64url')}.${hmac}`;

    return res.status(200).json({
      success: true,
      token,
      user: {
        name: 'Administrator',
        role: 'ADMINISTRATOR',
        institute: 'SRISHTI COMPUTER INSTITUTE'
      }
    });
  } catch (err) {
    return res.status(500).json({ success: false, error: err.message });
  }
}
