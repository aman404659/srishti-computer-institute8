import { COURSES_DATA } from './coursesData.js';

export default async function handler(req, res) {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET,OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  const { search, category, credential, popular } = req.query || {};

  let filtered = [...COURSES_DATA];

  if (popular === 'true') {
    filtered = filtered.filter(c => c.isPopular);
  }

  if (category && category !== 'All') {
    filtered = filtered.filter(c => c.category.toLowerCase() === category.toLowerCase());
  }

  if (credential && credential !== 'All' && credential !== 'All Credentials') {
    filtered = filtered.filter(c => {
      const name = c.nameEn.toLowerCase();
      if (credential === 'Master / PG Diploma') {
        return name.startsWith('post graduate') || name.startsWith('master diploma') || name.startsWith('post diploma');
      }
      if (credential === 'Advance Diploma') {
        return name.startsWith('advance diploma') || name.startsWith('advanced diploma') || name.startsWith('advance certificate');
      }
      if (credential === 'Diploma') {
        return name.startsWith('diploma') && !name.startsWith('advance diploma') && !name.startsWith('post graduate');
      }
      if (credential === 'Certificate') {
        return name.startsWith('certificate');
      }
      return true;
    });
  }

  if (search && search.trim()) {
    const q = search.trim().toLowerCase();
    filtered = filtered.filter(c =>
      c.nameEn.toLowerCase().includes(q) ||
      c.nameHi.toLowerCase().includes(q) ||
      c.code.toLowerCase().includes(q) ||
      c.category.toLowerCase().includes(q) ||
      c.syllabusSummary.toLowerCase().includes(q)
    );
  }

  return res.status(200).json({
    success: true,
    count: filtered.length,
    courses: filtered
  });
}
