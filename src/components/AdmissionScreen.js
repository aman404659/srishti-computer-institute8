import { COURSES_DATA } from '../data/coursesData.js';

export function renderAdmissionScreen({ preSelectedCourseName = '' } = {}) {
  const popularCourses = [
    'ADCA (Advance Diploma in Computer Application)',
    'DCA (Diploma in Computer Application)',
    'Certificate in TALLY Prime & GST',
    'Certificate in Electrician',
    'Certificate in Store Keeping',
    'Certificate in Debt Recovery Agent',
    'Diploma in Electrical',
    'Diploma in Mechanical',
    'Certificate in Diesel Mechanic',
    'Certificate in Montessori Teacher Training',
    'Early Childhood Care & Education (ECCE)',
    'Master Diploma in Computer Information System'
  ];

  return `
    <div class="screen-container">
      <div style="margin-bottom: 16px;">
        <h2 style="font-family: var(--font-heading); font-size: 20px; font-weight: 800; color: var(--white-pure);">
          Online Student Registration
        </h2>
        <div style="font-size: 12px; color: var(--neon-cyan);">
          Academic Session 2026 • Official Government Recognized Certification
        </div>
      </div>

      <!-- Backend Connection Status Indicator -->
      <div id="backend-status-indicator" class="alert alert-info" style="padding: 10px 14px; font-size: 12px; margin-bottom: 14px;">
        <span>🔒 Secure Registration: Direct sync with Institute Cloud Database</span>
      </div>

      <!-- Result Confirmation Banner (hidden initially) -->
      <div id="registration-success-card" class="card" style="display: none; border-color: var(--success-green); background: rgba(16, 185, 129, 0.08);">
        <!-- Filled on successful submission -->
      </div>

      <!-- Registration Form -->
      <div id="registration-form-card" class="card">
        <div class="card-title cyan">STUDENT ADMISSION APPLICATION</div>
        
        <form id="admission-form" novalidate>
          <div style="display: grid; grid-template-columns: 1fr; gap: 12px;">
            <!-- Full Name -->
            <div class="form-group">
              <label class="form-label">Full Name of Student <span class="req">*</span></label>
              <input type="text" id="reg-fullname" class="form-input" placeholder="e.g. Amit Kumar" required />
              <div class="form-error-msg" id="err-fullname"></div>
            </div>

            <!-- Father's Name -->
            <div class="form-group">
              <label class="form-label">Father\'s / Guardian\'s Name <span class="req">*</span></label>
              <input type="text" id="reg-fathername" class="form-input" placeholder="e.g. Suresh Kumar" required />
              <div class="form-error-msg" id="err-fathername"></div>
            </div>

            <!-- Phone Numbers Grid -->
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px;">
              <div class="form-group">
                <label class="form-label">Mobile Number <span class="req">*</span></label>
                <input type="tel" id="reg-phone" class="form-input" placeholder="10-digit number" maxlength="10" required />
                <div class="form-error-msg" id="err-phone"></div>
              </div>
              <div class="form-group">
                <label class="form-label">WhatsApp Number</label>
                <input type="tel" id="reg-whatsapp" class="form-input" placeholder="For batch updates" maxlength="10" />
              </div>
            </div>

            <!-- Email & DOB Grid -->
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px;">
              <div class="form-group">
                <label class="form-label">Email Address</label>
                <input type="email" id="reg-email" class="form-input" placeholder="student@example.com" />
                <div class="form-error-msg" id="err-email"></div>
              </div>
              <div class="form-group">
                <label class="form-label">Date of Birth <span class="req">*</span></label>
                <input type="date" id="reg-dob" class="form-input" required />
                <div class="form-error-msg" id="err-dob"></div>
              </div>
            </div>

            <!-- Gender & Qualification -->
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px;">
              <div class="form-group">
                <label class="form-label">Gender <span class="req">*</span></label>
                <select id="reg-gender" class="form-select">
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
                </select>
              </div>
              <div class="form-group">
                <label class="form-label">Highest Qualification <span class="req">*</span></label>
                <select id="reg-qualification" class="form-select">
                  <option value="10th Pass">10th Pass / High School</option>
                  <option value="12th Pass" selected>12th Pass / Intermediate</option>
                  <option value="Undergraduate">Undergraduate (Pursuing)</option>
                  <option value="Graduate">Graduate (BA, B.Com, B.Sc, BCA)</option>
                  <option value="Post Graduate">Post Graduate / Diploma</option>
                </select>
              </div>
            </div>

            <!-- Course Selection -->
            <div class="form-group">
              <label class="form-label">Course Selection <span class="req">*</span></label>
              <select id="reg-course" class="form-select" required>
                <option value="">-- Choose a Course --</option>
                <optgroup label="Popular Certification & Diplomas">
                  ${popularCourses.map(p => `<option value="${p}" ${preSelectedCourseName === p ? 'selected' : ''}>${p}</option>`).join('')}
                </optgroup>
                <optgroup label="All Available Certified Courses">
                  ${COURSES_DATA.map(c => `<option value="${c.nameEn} (${c.code})" ${preSelectedCourseName.includes(c.code) ? 'selected' : ''}>${c.nameEn} [${c.code} - ₹${c.fee}]</option>`).join('')}
                </optgroup>
              </select>
              <div class="form-error-msg" id="err-course"></div>
            </div>

            <!-- Batch Timing -->
            <div class="form-group">
              <label class="form-label">Preferred Batch Timing <span class="req">*</span></label>
              <select id="reg-batch" class="form-select" required>
                <option value="Morning (08:00 AM - 10:00 AM)">Morning: 08:00 AM - 10:00 AM</option>
                <option value="Mid-day (11:00 AM - 01:00 PM)">Mid-day: 11:00 AM - 01:00 PM</option>
                <option value="Afternoon (02:00 PM - 04:00 PM)">Afternoon: 02:00 PM - 04:00 PM</option>
                <option value="Evening (04:00 PM - 06:00 PM)">Evening: 04:00 PM - 06:00 PM</option>
              </select>
            </div>

            <!-- Address -->
            <div class="form-group">
              <label class="form-label">Full Residential Address <span class="req">*</span></label>
              <textarea id="reg-address" class="form-textarea" rows="2" placeholder="House/Village, Post, Tehsil/City, District, State, PIN" required></textarea>
              <div class="form-error-msg" id="err-address"></div>
            </div>

            <!-- Terms Confirmation -->
            <div style="font-size: 11px; color: var(--text-muted); background: var(--navy-surface); padding: 10px; border-radius: 8px; border: 1px solid var(--navy-border);">
              ℹ️ By submitting this application, you confirm that details provided are accurate. Srishti Computer Institute will verify your admission and issue your official student enrollment slip.
            </div>

            <button type="submit" id="btn-submit-registration" class="btn btn-primary btn-full" style="margin-top: 10px; font-size: 15px;">
              🚀 Submit Admission Application
            </button>
          </div>
        </form>
      </div>
    </div>
  `;
}
