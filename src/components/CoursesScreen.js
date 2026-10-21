import { COURSES_DATA } from '../data/coursesData.js';

export function renderCoursesScreen({ searchQuery = '', selectedCategory = 'All', selectedCredential = 'All' } = {}) {
  const categories = [
    'All',
    'Computer & IT',
    'Finance & Accounting',
    'Technical & Engineering',
    'Teaching & Sports',
    'Vocational & Safety',
    'Medical & Healthcare',
    'Media & Animation',
    'Beauty & Fashion',
    'Agriculture & Farming'
  ];

  const credentials = [
    'All',
    'Certificate',
    'Diploma',
    'Advance Diploma',
    'Master / PG Diploma'
  ];

  return `
    <div class="screen-container">
      <div style="margin-bottom: 16px;">
        <h2 style="font-family: var(--font-heading); font-size: 20px; font-weight: 800; color: var(--white-pure);">
          Certified Courses Catalog
        </h2>
        <div style="font-size: 12px; color: var(--neon-cyan);">
          Browse all 290+ Govt. Registered & Vocational Certification Programs
        </div>
      </div>

      <!-- Search & Filters -->
      <div class="card" style="padding: 16px; margin-bottom: 16px;">
        <div class="form-group" style="margin-bottom: 12px;">
          <input 
            type="text" 
            id="course-search-input" 
            class="form-input" 
            placeholder="🔍 Search courses by name, code (e.g. AI03, ADCA, Tally)..." 
            value="${escapeHtml(searchQuery)}"
          />
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px;">
          <div>
            <label class="form-label" style="font-size: 11px;">Category</label>
            <select id="course-category-select" class="form-select" style="font-size: 12px;">
              ${categories.map(c => `<option value="${c}" ${selectedCategory === c ? 'selected' : ''}>${c}</option>`).join('')}
            </select>
          </div>
          <div>
            <label class="form-label" style="font-size: 11px;">Credential Level</label>
            <select id="course-credential-select" class="form-select" style="font-size: 12px;">
              ${credentials.map(c => `<option value="${c}" ${selectedCredential === c ? 'selected' : ''}>${c}</option>`).join('')}
            </select>
          </div>
        </div>
      </div>

      <!-- Results Count & Active Filters Indicator -->
      <div id="course-results-container">
        <!-- Rendered dynamically via updateCourseList() -->
      </div>
    </div>
  `;
}

function escapeHtml(str) {
  if (!str) return '';
  return str.replace(/[&<>"']/g, m => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;'
  })[m]);
}

export function filterCourses({ searchQuery = '', category = 'All', credential = 'All' } = {}) {
  let list = COURSES_DATA;

  if (category && category !== 'All') {
    list = list.filter(c => c.category.toLowerCase() === category.toLowerCase());
  }

  if (credential && credential !== 'All') {
    list = list.filter(c => {
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

  if (searchQuery && searchQuery.trim()) {
    const q = searchQuery.trim().toLowerCase();
    list = list.filter(c =>
      c.nameEn.toLowerCase().includes(q) ||
      c.nameHi.toLowerCase().includes(q) ||
      c.code.toLowerCase().includes(q) ||
      c.category.toLowerCase().includes(q) ||
      c.syllabusSummary.toLowerCase().includes(q)
    );
  }

  return list;
}

export function renderCoursesListHtml(courses) {
  if (courses.length === 0) {
    return `
      <div class="card" style="text-align: center; padding: 36px 16px;">
        <div style="font-size: 32px; margin-bottom: 8px;">🔍</div>
        <div style="font-weight: 700; font-size: 15px;">No matching courses found</div>
        <div style="font-size: 12px; color: var(--text-muted); margin-top: 4px;">Try searching with a different term or resetting the category filter.</div>
      </div>
    `;
  }

  return `
    <div style="font-size: 12px; color: var(--text-muted); margin-bottom: 12px; font-weight: 600;">
      Showing <span style="color: var(--neon-cyan);">${courses.length}</span> course${courses.length > 1 ? 's' : ''}
    </div>
    <div style="display: flex; flex-direction: column; gap: 12px;">
      ${courses.map(course => `
        <div class="card" style="padding: 16px; margin-bottom: 0; background: var(--navy-midnight); border-color: var(--navy-border);">
          <div style="display: flex; justify-content: space-between; align-items: flex-start; gap: 10px;">
            <div style="flex: 1;">
              <div style="display: flex; align-items: center; gap: 6px; margin-bottom: 4px; flex-wrap: wrap;">
                <span class="badge badge-cyan" style="font-size: 10px;">${course.code}</span>
                <span class="badge badge-orange" style="font-size: 10px;">${course.category}</span>
                ${course.isPopular ? '<span class="badge badge-green" style="font-size: 10px;">POPULAR</span>' : ''}
              </div>
              <h3 style="font-size: 15px; font-weight: 700; color: var(--white-pure); margin-bottom: 2px;">
                ${course.nameEn}
              </h3>
              <div style="font-size: 12px; color: var(--text-muted); margin-bottom: 8px;">
                ${course.nameHi}
              </div>
            </div>
            
            <div style="text-align: right; min-width: 90px;">
              <div style="font-size: 10px; color: var(--text-muted);">Fee Structure</div>
              <div style="font-size: 16px; font-weight: 800; color: #FFD166;">₹${course.fee.toLocaleString('en-IN')}</div>
            </div>
          </div>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 8px; background: var(--navy-surface); border-radius: 8px; padding: 8px 12px; margin: 10px 0; font-size: 11px;">
            <div><span style="color: var(--text-muted);">Duration:</span> <strong style="color: var(--white-pure);">${course.durationEn}</strong></div>
            <div><span style="color: var(--text-muted);">Eligibility:</span> <strong style="color: var(--white-pure);">${course.eligibilityEn}</strong></div>
          </div>

          <div style="display: flex; gap: 8px; margin-top: 10px;">
            <button data-course-detail="${course.id}" class="btn btn-secondary" style="flex: 1; min-height: 36px; padding: 6px 10px; font-size: 12px;">
              📖 Syllabus Details
            </button>
            <button data-apply-course="${course.nameEn}" class="btn btn-primary" style="flex: 1; min-height: 36px; padding: 6px 10px; font-size: 12px;">
              ✍️ Apply Now
            </button>
          </div>
        </div>
      `).join('')}
    </div>
  `;
}
