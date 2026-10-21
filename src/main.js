import './styles/app.css';
import { COURSES_DATA } from './data/coursesData.js';
import { 
  submitRegistration, 
  searchStudentApplication, 
  adminLogin, 
  getAdminToken, 
  adminLogout, 
  fetchAdminRegistrations, 
  updateApplicationStatus 
} from './data/store.js';

import { renderHomeScreen } from './components/HomeScreen.js';
import { renderCoursesScreen, filterCourses, renderCoursesListHtml } from './components/CoursesScreen.js';
import { renderAdmissionScreen } from './components/AdmissionScreen.js';
import { renderStudentPortalScreen, renderStudentResultCard } from './components/StudentPortalScreen.js';
import { renderAboutContactScreen } from './components/AboutContactScreen.js';
import { renderAdminPanelScreen } from './components/AdminPanelScreen.js';
import { renderCourseModal } from './components/CourseModal.js';

// Application State
let currentRoute = 'home';
let selectedCourseForAdmission = '';
let courseFilterState = { searchQuery: '', category: 'All', credential: 'All' };
let adminFilterState = { activeTab: 'ALL', applications: [] };
let activeModalCourse = null;
let deferredPrompt = null;

// PWA Service Worker Registration & Installation
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js').catch(err => {
      console.log('SW registration note:', err);
    });
  });
}

window.addEventListener('beforeinstallprompt', (e) => {
  e.preventDefault();
  deferredPrompt = e;
  const pwaBanner = document.getElementById('pwa-install-banner');
  if (pwaBanner) pwaBanner.style.display = 'flex';
});

// Navigation & Routing
function navigateTo(route, options = {}) {
  currentRoute = route;
  if (options.courseName) {
    selectedCourseForAdmission = options.courseName;
  }
  window.location.hash = `#${route}`;
  renderApp();
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function getRouteFromHash() {
  const hash = window.location.hash.replace('#', '').trim();
  const validRoutes = ['home', 'courses', 'admission', 'student-portal', 'contact', 'admin'];
  return validRoutes.includes(hash) ? hash : 'home';
}

window.addEventListener('hashchange', () => {
  const newRoute = getRouteFromHash();
  if (newRoute !== currentRoute) {
    currentRoute = newRoute;
    renderApp();
  }
});

// Main Render Function
function renderApp() {
  const app = document.getElementById('app');
  if (!app) return;

  const isHome = currentRoute === 'home';
  const isCourses = currentRoute === 'courses';
  const isAdmission = currentRoute === 'admission';
  const isPortal = currentRoute === 'student-portal';
  const isContact = currentRoute === 'contact';
  const isAdmin = currentRoute === 'admin';

  app.innerHTML = `
    <!-- App Header -->
    <header class="app-header">
      <div class="header-inner">
        <div class="header-brand" id="brand-home-click">
          <div class="header-logo">
            <img src="/logo.png" alt="Srishti Logo" />
          </div>
          <div class="brand-text">
            <h1>SRISHTI COMPUTER INSTITUTE</h1>
            <div class="tagline">LEARN • PRACTICE • GROW</div>
          </div>
        </div>

        <nav class="desktop-nav">
          <a href="#home" class="${isHome ? 'active' : ''}">Home</a>
          <a href="#courses" class="${isCourses ? 'active' : ''}">Courses</a>
          <a href="#admission" class="${isAdmission ? 'active' : ''}">Admission</a>
          <a href="#student-portal" class="${isPortal ? 'active' : ''}">Student Portal</a>
          <a href="#contact" class="${isContact ? 'active' : ''}">Contact</a>
          <a href="#admin" class="${isAdmin ? 'active' : ''}" style="color: var(--orange-accent);">Admin</a>
        </nav>

        <button class="admin-header-btn" id="header-admin-btn">
          🔒 Admin
        </button>
      </div>
    </header>

    <!-- Main Content Container -->
    <main class="main-content" id="main-content">
      ${renderScreenContent()}
    </main>

    <!-- Mobile Bottom Navigation Bar -->
    <nav class="bottom-nav">
      <div class="bottom-nav-item ${isHome ? 'active' : ''}" data-nav="home">
        <span class="icon">🏠</span>
        <span>Home</span>
      </div>
      <div class="bottom-nav-item ${isCourses ? 'active' : ''}" data-nav="courses">
        <span class="icon">📚</span>
        <span>Courses</span>
      </div>
      <div class="bottom-nav-item ${isAdmission ? 'active' : ''}" data-nav="admission">
        <span class="icon">✍️</span>
        <span>Admission</span>
      </div>
      <div class="bottom-nav-item ${isPortal ? 'active' : ''}" data-nav="student-portal">
        <span class="icon">🎓</span>
        <span>Portal</span>
      </div>
      <div class="bottom-nav-item ${isContact ? 'active' : ''}" data-nav="contact">
        <span class="icon">📞</span>
        <span>Contact</span>
      </div>
    </nav>

    <!-- Modal Container -->
    <div id="modal-root">
      ${activeModalCourse ? renderCourseModal(activeModalCourse) : ''}
    </div>
  `;

  attachEventListeners();
}

function renderScreenContent() {
  switch (currentRoute) {
    case 'home':
      return renderHomeScreen({
        onNavigate: navigateTo,
        onSelectCourse: (c) => navigateTo('admission', { courseName: c })
      });
    case 'courses':
      return renderCoursesScreen({
        searchQuery: courseFilterState.searchQuery,
        selectedCategory: courseFilterState.category,
        selectedCredential: courseFilterState.credential
      });
    case 'admission':
      return renderAdmissionScreen({
        preSelectedCourseName: selectedCourseForAdmission
      });
    case 'student-portal':
      return renderStudentPortalScreen();
    case 'contact':
      return renderAboutContactScreen();
    case 'admin':
      return renderAdminPanelScreen({
        isAuthenticated: !!getAdminToken(),
        applications: adminFilterState.applications,
        activeTab: adminFilterState.activeTab
      });
    default:
      return renderHomeScreen({ onNavigate: navigateTo });
  }
}

// Event Listeners for Current View
function attachEventListeners() {
  // Brand Header Click
  document.getElementById('brand-home-click')?.addEventListener('click', () => navigateTo('home'));
  document.getElementById('header-admin-btn')?.addEventListener('click', () => navigateTo('admin'));

  // Mobile Bottom Nav items
  document.querySelectorAll('.bottom-nav-item').forEach(item => {
    item.addEventListener('click', () => {
      const target = item.getAttribute('data-nav');
      if (target) navigateTo(target);
    });
  });

  // Generic data-nav buttons
  document.querySelectorAll('[data-nav]').forEach(btn => {
    btn.addEventListener('click', () => {
      const target = btn.getAttribute('data-nav');
      if (target) navigateTo(target);
    });
  });

  // Direct Apply Course Buttons
  document.querySelectorAll('[data-apply-course]').forEach(btn => {
    btn.addEventListener('click', () => {
      const courseName = btn.getAttribute('data-apply-course');
      activeModalCourse = null;
      navigateTo('admission', { courseName });
    });
  });

  // PWA Install Button
  const installBanner = document.getElementById('pwa-install-banner');
  const installBtn = document.getElementById('pwa-install-btn');
  if (installBanner && deferredPrompt) {
    installBanner.style.display = 'flex';
  }
  installBtn?.addEventListener('click', async () => {
    if (deferredPrompt) {
      deferredPrompt.prompt();
      const choiceResult = await deferredPrompt.userChoice;
      if (choiceResult.outcome === 'accepted') {
        if (installBanner) installBanner.style.display = 'none';
      }
      deferredPrompt = null;
    }
  });

  // Modal Close & Background Click
  document.getElementById('modal-close-btn')?.addEventListener('click', () => {
    activeModalCourse = null;
    renderApp();
  });
  document.getElementById('course-modal-overlay')?.addEventListener('click', (e) => {
    if (e.target.id === 'course-modal-overlay') {
      activeModalCourse = null;
      renderApp();
    }
  });

  // View Course Details in Courses Screen
  document.querySelectorAll('[data-course-detail]').forEach(btn => {
    btn.addEventListener('click', () => {
      const courseId = btn.getAttribute('data-course-detail');
      const found = COURSES_DATA.find(c => c.id === courseId);
      if (found) {
        activeModalCourse = found;
        renderApp();
      }
    });
  });

  // Specific Screen Handlers
  if (currentRoute === 'courses') {
    initCoursesScreenHandlers();
  } else if (currentRoute === 'admission') {
    initAdmissionScreenHandlers();
  } else if (currentRoute === 'student-portal') {
    initStudentPortalHandlers();
  } else if (currentRoute === 'admin') {
    initAdminPanelHandlers();
  }
}

// 1. Courses Screen Handlers
function initCoursesScreenHandlers() {
  const searchInput = document.getElementById('course-search-input');
  const categorySelect = document.getElementById('course-category-select');
  const credentialSelect = document.getElementById('course-credential-select');
  const resultsContainer = document.getElementById('course-results-container');

  const updateResults = () => {
    courseFilterState.searchQuery = searchInput?.value || '';
    courseFilterState.category = categorySelect?.value || 'All';
    courseFilterState.credential = credentialSelect?.value || 'All';

    const filtered = filterCourses(courseFilterState);
    if (resultsContainer) {
      resultsContainer.innerHTML = renderCoursesListHtml(filtered);

      // Re-bind click events
      resultsContainer.querySelectorAll('[data-course-detail]').forEach(btn => {
        btn.addEventListener('click', () => {
          const courseId = btn.getAttribute('data-course-detail');
          const found = COURSES_DATA.find(c => c.id === courseId);
          if (found) {
            activeModalCourse = found;
            renderApp();
          }
        });
      });

      resultsContainer.querySelectorAll('[data-apply-course]').forEach(btn => {
        btn.addEventListener('click', () => {
          const courseName = btn.getAttribute('data-apply-course');
          navigateTo('admission', { courseName });
        });
      });
    }
  };

  searchInput?.addEventListener('input', updateResults);
  categorySelect?.addEventListener('change', updateResults);
  credentialSelect?.addEventListener('change', updateResults);

  // Initial populate
  updateResults();
}

// 2. Admission Screen Handlers
function initAdmissionScreenHandlers() {
  const form = document.getElementById('admission-form');
  const successCard = document.getElementById('registration-success-card');
  const formCard = document.getElementById('registration-form-card');

  form?.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Clear previous errors
    document.querySelectorAll('.form-error-msg').forEach(el => el.textContent = '');
    document.querySelectorAll('.form-input').forEach(el => el.classList.remove('is-invalid'));

    const fullName = document.getElementById('reg-fullname')?.value.trim();
    const fatherName = document.getElementById('reg-fathername')?.value.trim();
    const phone = document.getElementById('reg-phone')?.value.replace(/\D/g, '');
    const whatsapp = document.getElementById('reg-whatsapp')?.value.replace(/\D/g, '') || phone;
    const email = document.getElementById('reg-email')?.value.trim();
    const dob = document.getElementById('reg-dob')?.value;
    const gender = document.getElementById('reg-gender')?.value;
    const qualification = document.getElementById('reg-qualification')?.value;
    const courseName = document.getElementById('reg-course')?.value;
    const batchTiming = document.getElementById('reg-batch')?.value;
    const address = document.getElementById('reg-address')?.value.trim();

    let hasError = false;

    if (!fullName || fullName.length < 3) {
      setError('err-fullname', 'reg-fullname', 'Full name is required (min 3 letters)');
      hasError = true;
    }

    if (!fatherName) {
      setError('err-fathername', 'reg-fathername', "Father's name is required");
      hasError = true;
    }

    if (!phone || phone.length !== 10) {
      setError('err-phone', 'reg-phone', 'Enter a valid 10-digit mobile number');
      hasError = true;
    }

    if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setError('err-email', 'reg-email', 'Enter a valid email address');
      hasError = true;
    }

    if (!dob) {
      setError('err-dob', 'reg-dob', 'Date of birth is required');
      hasError = true;
    }

    if (!courseName) {
      setError('err-course', 'reg-course', 'Please select a course from the list');
      hasError = true;
    }

    if (!address || address.length < 5) {
      setError('err-address', 'reg-address', 'Please provide complete residential address');
      hasError = true;
    }

    if (hasError) return;

    const submitBtn = document.getElementById('btn-submit-registration');
    if (submitBtn) {
      submitBtn.disabled = true;
      submitBtn.textContent = '⏳ Submitting to Institute Database...';
    }

    try {
      const result = await submitRegistration({
        fullName,
        fatherName,
        phone,
        whatsapp,
        email,
        dob,
        gender,
        qualification,
        courseName,
        batchTiming,
        address
      });

      if (result.success && result.application) {
        const app = result.application;
        if (formCard) formCard.style.display = 'none';
        if (successCard) {
          successCard.style.display = 'block';
          successCard.innerHTML = `
            <div style="text-align: center; margin-bottom: 16px;">
              <div style="font-size: 42px; margin-bottom: 6px;">🎉</div>
              <h3 style="font-family: var(--font-heading); font-size: 19px; font-weight: 800; color: var(--success-green); margin-bottom: 2px;">
                Admission Application Registered!
              </h3>
              <div style="font-size: 12px; color: var(--text-secondary);">
                ${result.source === 'cloud-serverless' ? 'Saved securely to Institute Cloud Server' : 'Saved to Local Device Storage (Syncs automatically)'}
              </div>
            </div>

            <div style="background: var(--navy-midnight); border: 2px dashed var(--neon-cyan); border-radius: 12px; padding: 16px; margin-bottom: 16px;">
              <div style="text-align: center; margin-bottom: 12px; border-bottom: 1px solid var(--navy-border); padding-bottom: 8px;">
                <div style="font-size: 11px; color: var(--text-muted); text-transform: uppercase;">Official Reference Number</div>
                <div style="font-family: var(--font-heading); font-size: 22px; font-weight: 800; color: var(--neon-cyan); letter-spacing: 1px;">
                  ${app.referenceNo}
                </div>
              </div>

              <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 8px; font-size: 12px;">
                <div><span style="color: var(--text-muted);">Student Name:</span> <strong>${app.fullName}</strong></div>
                <div><span style="color: var(--text-muted);">Father's Name:</span> <strong>${app.fatherName}</strong></div>
                <div><span style="color: var(--text-muted);">Contact Phone:</span> <strong>${app.phone}</strong></div>
                <div><span style="color: var(--text-muted);">Batch Timing:</span> <strong>${app.batchTiming}</strong></div>
                <div style="grid-column: span 2;"><span style="color: var(--text-muted);">Selected Course:</span> <strong style="color: #FFD166;">${app.courseName}</strong></div>
              </div>
            </div>

            <div style="font-size: 12px; color: var(--text-secondary); background: var(--navy-surface); padding: 12px; border-radius: 8px; margin-bottom: 16px; line-height: 1.5;">
              📌 <strong>Next Steps:</strong> Please visit our campus at <em>Bagdaudhi Kachhar Kothi, Mandhana, Kanpur Nagar</em> with original academic marksheets and 2 passport photos to confirm your seat batch.
            </div>

            <div style="display: flex; gap: 8px; flex-wrap: wrap;">
              <button onclick="window.print()" class="btn btn-secondary" style="flex: 1; font-size: 12px;">
                🖨️ Print Application Slip
              </button>
              <a href="https://wa.me/918953994687?text=${encodeURIComponent(`Hello Srishti Institute, I have submitted an online admission application. Reference No: ${app.referenceNo}`)}" target="_blank" rel="noopener noreferrer" class="btn btn-success" style="flex: 1; font-size: 12px;">
                💬 Confirm on WhatsApp
              </a>
            </div>

            <button id="btn-submit-another" class="btn btn-cyan btn-full" style="margin-top: 10px; font-size: 12px;">
              ➕ Submit Another Registration
            </button>
          `;

          document.getElementById('btn-submit-another')?.addEventListener('click', () => {
            selectedCourseForAdmission = '';
            renderApp();
          });
        }
      }
    } catch (err) {
      alert('Error submitting registration: ' + err.message);
      if (submitBtn) {
        submitBtn.disabled = false;
        submitBtn.textContent = '🚀 Submit Admission Application';
      }
    }
  });

  function setError(errorId, inputId, msg) {
    const errorEl = document.getElementById(errorId);
    const inputEl = document.getElementById(inputId);
    if (errorEl) errorEl.textContent = msg;
    if (inputEl) inputEl.classList.add('is-invalid');
  }
}

// 3. Student Portal Handlers
function initStudentPortalHandlers() {
  const form = document.getElementById('student-search-form');
  const input = document.getElementById('student-search-input');
  const resultsContainer = document.getElementById('student-search-results');

  form?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const q = input?.value.trim();
    if (!q) return;

    if (resultsContainer) {
      resultsContainer.innerHTML = '<div style="text-align: center; padding: 24px; color: var(--neon-cyan);">Searching Institute Database...</div>';
    }

    const apps = await searchStudentApplication(q);
    if (!resultsContainer) return;

    if (apps.length === 0) {
      resultsContainer.innerHTML = `
        <div class="card" style="text-align: center; padding: 28px 16px;">
          <div style="font-size: 32px; margin-bottom: 6px;">🔎</div>
          <div style="font-weight: 700; font-size: 15px;">No record found for "${q}"</div>
          <div style="font-size: 12px; color: var(--text-muted); margin-top: 4px;">
            Please check your reference number (e.g. SCI-2026-XXXX) or registered 10-digit mobile number.
          </div>
          <a href="tel:8953994687" class="btn btn-cyan" style="margin-top: 12px; font-size: 12px;">
            📞 Contact Institute Helpline: 8953994687
          </a>
        </div>
      `;
    } else {
      resultsContainer.innerHTML = apps.map(renderStudentResultCard).join('');
    }
  });
}

// 4. Admin Panel Handlers
async function initAdminPanelHandlers() {
  const loginForm = document.getElementById('admin-login-form');
  const loginError = document.getElementById('admin-login-error');

  // Handle Login
  loginForm?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const password = document.getElementById('admin-password')?.value;
    const btn = document.getElementById('btn-admin-login');

    if (btn) {
      btn.disabled = true;
      btn.textContent = 'Verifying Authenticity...';
    }
    if (loginError) loginError.style.display = 'none';

    const res = await adminLogin(password);
    if (res.success) {
      await refreshAdminData();
      renderApp();
    } else {
      if (loginError) {
        loginError.textContent = res.error || 'Invalid admin credentials.';
        loginError.style.display = 'block';
      }
      if (btn) {
        btn.disabled = false;
        btn.textContent = 'Sign In to Admin Dashboard';
      }
    }
  });

  // If already authenticated, fetch registrations
  if (getAdminToken()) {
    if (adminFilterState.applications.length === 0) {
      await refreshAdminData();
      renderApp();
      return;
    }

    // Tab buttons
    document.querySelectorAll('[data-admin-tab]').forEach(btn => {
      btn.addEventListener('click', () => {
        adminFilterState.activeTab = btn.getAttribute('data-admin-tab') || 'ALL';
        renderApp();
      });
    });

    // Sign out button
    document.getElementById('admin-logout-btn')?.addEventListener('click', () => {
      adminLogout();
      adminFilterState.applications = [];
      renderApp();
    });

    // Export CSV
    document.getElementById('admin-export-btn')?.addEventListener('click', () => {
      exportRegistrationsCSV(adminFilterState.applications);
    });

    // Action buttons: Approve / Reject
    document.querySelectorAll('[data-admin-action]').forEach(btn => {
      btn.addEventListener('click', async () => {
        const action = btn.getAttribute('data-admin-action');
        const appId = btn.getAttribute('data-app-id');
        if (!appId) return;

        if (action === 'approve') {
          const remarks = prompt('Enter approval remarks (or leave blank for standard confirmation):', 'Approved for Batch Induction') || 'Approved';
          btn.textContent = 'Approving...';
          await updateApplicationStatus(appId, 'APPROVED', remarks);
          await refreshAdminData();
          renderApp();
        } else if (action === 'reject') {
          const remarks = prompt('Enter rejection reason:', 'Document verification mismatch') || 'Rejected';
          btn.textContent = 'Rejecting...';
          await updateApplicationStatus(appId, 'REJECTED', remarks);
          await refreshAdminData();
          renderApp();
        }
      });
    });
  }
}

async function refreshAdminData() {
  try {
    const list = await fetchAdminRegistrations();
    adminFilterState.applications = list || [];
  } catch (e) {
    if (e.message.includes('session expired')) {
      alert(e.message);
      renderApp();
    }
  }
}

function exportRegistrationsCSV(items) {
  if (!items || items.length === 0) {
    alert('No registrations available to export.');
    return;
  }

  const headers = ['Reference No', 'Enrollment No', 'Status', 'Full Name', "Father's Name", 'Mobile', 'Course', 'Batch', 'Fee', 'Address', 'Applied Date'];
  const rows = items.map(i => [
    `"${i.referenceNo || ''}"`,
    `"${i.enrollmentNo || ''}"`,
    `"${i.status || ''}"`,
    `"${i.fullName || ''}"`,
    `"${i.fatherName || ''}"`,
    `"${i.phone || ''}"`,
    `"${i.courseName || ''}"`,
    `"${i.batchTiming || ''}"`,
    `"${i.fee || 1299}"`,
    `"${(i.address || '').replace(/"/g, '""')}"`,
    `"${new Date(i.appliedAtMillis || Date.now()).toISOString().split('T')[0]}"`
  ]);

  const csvContent = 'data:text/csv;charset=utf-8,' + [headers.join(','), ...rows.map(e => e.join(','))].join('\n');
  const encodedUri = encodeURI(csvContent);
  const link = document.createElement('a');
  link.setAttribute('href', encodedUri);
  link.setAttribute('download', `Srishti_Institute_Registrations_${new Date().toISOString().split('T')[0]}.csv`);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

// Initial Boot
currentRoute = getRouteFromHash();
renderApp();
