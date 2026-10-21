const BASE_URL = (typeof import.meta !== 'undefined' && import.meta.env && import.meta.env.BASE_URL) ? import.meta.env.BASE_URL : './';

export function renderHomeScreen({ onNavigate, onSelectCourse }) {
  return `
    <div class="screen-container">
      <!-- PWA Install Banner (Android) -->
      <div id="pwa-install-banner" class="pwa-banner" style="display: none;">
        <div style="display: flex; align-items: center; gap: 10px;">
          <img src="${BASE_URL}logo.png" style="width: 38px; height: 38px; border-radius: 50%; border: 1.5px solid var(--orange-accent); background: white;" alt="Icon" />
          <div>
            <div style="font-weight: 700; font-size: 13px;">Install Srishti Android App</div>
            <div style="font-size: 11px; color: var(--text-muted);">Fast access, offline status & notifications</div>
          </div>
        </div>
        <button id="pwa-install-btn" class="btn btn-primary" style="min-height: 38px; padding: 6px 14px; font-size: 12px;">Install</button>
      </div>

      <!-- Hero Header Card -->
      <section class="card hero-section" style="text-align: center; padding: 28px 18px; border-color: rgba(0, 229, 255, 0.3);">
        <div style="width: 96px; height: 96px; border-radius: 50%; background: #ffffff; border: 3px solid var(--orange-accent); margin: 0 auto 14px; padding: 3px; display: flex; align-items: center; justify-content: center; box-shadow: 0 0 20px rgba(255, 107, 0, 0.35);">
          <img src="${BASE_URL}logo.png" alt="Srishti Computer Institute Logo" style="width: 100%; height: 100%; object-fit: contain; border-radius: 50%;" />
        </div>
        
        <h2 style="font-family: var(--font-heading); font-size: 22px; font-weight: 800; letter-spacing: 0.5px; margin-bottom: 6px;">
          SRISHTI COMPUTER INSTITUTE
        </h2>
        
        <div class="badge badge-orange" style="margin-bottom: 12px; font-size: 11px; letter-spacing: 1.5px;">
          LEARN • PRACTICE • GROW
        </div>

        <p style="font-size: 13px; color: var(--text-secondary); max-width: 580px; margin: 0 auto 16px;">
          Govt. Registered Training Institute offering 290+ Certified Professional, Technical & Vocational Courses with 100% Hands-on Practical Lab.
        </p>

        <!-- Quick 4 Action Buttons -->
        <div class="actions-grid">
          <a href="tel:8953994687" class="btn btn-cyan">
            📞 Call Now
          </a>
          <a href="https://wa.me/918953994687" target="_blank" rel="noopener noreferrer" class="btn btn-success">
            💬 WhatsApp
          </a>
          <button data-nav="admission" class="btn btn-primary">
            ✍️ Admission
          </button>
          <button data-nav="courses" class="btn btn-secondary">
            📚 All Courses
          </button>
        </div>
      </section>

      <!-- Official Institute Portal Links -->
      <section class="card">
        <div class="card-title cyan">OFFICIAL INSTITUTE LINKS & WEBSITES</div>
        <div style="display: flex; flex-direction: column; gap: 10px;">
          <a href="https://sites.google.com/view/srishti-computer-institute/home" target="_blank" rel="noopener noreferrer" class="btn btn-secondary btn-full" style="justify-content: flex-start; border-color: var(--neon-cyan);">
            🌐 Visit Our Website: <span style="color: var(--neon-cyan); margin-left: 6px; font-weight: 600; font-size: 12px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">sites.google.com/view/srishti-computer-institute</span>
          </a>

          <a href="https://share.google/tSYX6LZlGeJInp8cv" target="_blank" rel="noopener noreferrer" class="btn btn-secondary btn-full" style="justify-content: flex-start; border-color: var(--orange-accent);">
            📍 View Institute on Google Maps: <span style="color: var(--orange-accent); margin-left: 6px; font-weight: 600; font-size: 12px;">Bagdaudhi Kachhar Kothi, Mandhana</span>
          </a>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-top: 4px;">
            <a href="https://chat.whatsapp.com/L9Z8gxRL7QX3dJD3EEMBqO?s=cl&p=a&mlu=4&ilr=4&iam=2" target="_blank" rel="noopener noreferrer" class="btn btn-secondary" style="border-color: var(--success-green); font-size: 12px;">
              👥 Join WhatsApp Group
            </a>
            <a href="https://t.me/SrishtiComputerInstitute" target="_blank" rel="noopener noreferrer" class="btn btn-secondary" style="border-color: #0088CC; font-size: 12px;">
              ✈️ Join Telegram Channel
            </a>
          </div>
        </div>
      </section>

      <!-- Key Institute Highlights Metrics -->
      <section style="display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 10px; margin-bottom: 16px;">
        <div class="card" style="padding: 14px; text-align: center; margin-bottom: 0;">
          <div style="font-size: 24px; font-weight: 800; color: var(--neon-cyan); font-family: var(--font-heading);">290+</div>
          <div style="font-size: 11px; color: var(--text-muted); font-weight: 600;">Certified Courses</div>
        </div>
        <div class="card" style="padding: 14px; text-align: center; margin-bottom: 0;">
          <div style="font-size: 24px; font-weight: 800; color: var(--orange-accent); font-family: var(--font-heading);">1,200+</div>
          <div style="font-size: 11px; color: var(--text-muted); font-weight: 600;">Students Trained</div>
        </div>
        <div class="card" style="padding: 14px; text-align: center; margin-bottom: 0;">
          <div style="font-size: 24px; font-weight: 800; color: var(--success-green); font-family: var(--font-heading);">100%</div>
          <div style="font-size: 11px; color: var(--text-muted); font-weight: 600;">Practical Labs</div>
        </div>
        <div class="card" style="padding: 14px; text-align: center; margin-bottom: 0;">
          <div style="font-size: 24px; font-weight: 800; color: #FFD166; font-family: var(--font-heading);">4.9 ★</div>
          <div style="font-size: 11px; color: var(--text-muted); font-weight: 600;">Student Rating</div>
        </div>
      </section>

      <!-- Popular Courses Fee Structure -->
      <section class="card">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;">
          <div class="card-title cyan" style="margin-bottom: 0;">POPULAR COURSES & FEES</div>
          <button data-nav="courses" class="btn btn-cyan" style="min-height: 32px; padding: 4px 10px; font-size: 11px;">View All 290+</button>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 10px;">
          <!-- Course Card 1 -->
          <div class="card" style="background: var(--navy-surface); margin-bottom: 0; padding: 14px; display: flex; justify-content: space-between; align-items: center;">
            <div>
              <div style="font-weight: 700; font-size: 13px;">ADCA (Advance Diploma in Computer App)</div>
              <div style="font-size: 11px; color: var(--neon-cyan);">AI03 • 12 Months • 10th/12th Pass</div>
            </div>
            <div style="text-align: right;">
              <div style="font-weight: 800; color: #FFD166; font-size: 14px;">₹1,299</div>
              <button data-apply-course="ADCA (Advance Diploma in Computer App)" class="btn btn-primary" style="min-height: 28px; padding: 2px 8px; font-size: 11px; margin-top: 4px;">Apply</button>
            </div>
          </div>

          <!-- Course Card 2 -->
          <div class="card" style="background: var(--navy-surface); margin-bottom: 0; padding: 14px; display: flex; justify-content: space-between; align-items: center;">
            <div>
              <div style="font-weight: 700; font-size: 13px;">DCA (Diploma in Computer Application)</div>
              <div style="font-size: 11px; color: var(--neon-cyan);">AI01 • 6 Months • 10th/12th Pass</div>
            </div>
            <div style="text-align: right;">
              <div style="font-weight: 800; color: #FFD166; font-size: 14px;">₹1,299</div>
              <button data-apply-course="DCA (Diploma in Computer Application)" class="btn btn-primary" style="min-height: 28px; padding: 2px 8px; font-size: 11px; margin-top: 4px;">Apply</button>
            </div>
          </div>

          <!-- Course Card 3 -->
          <div class="card" style="background: var(--navy-surface); margin-bottom: 0; padding: 14px; display: flex; justify-content: space-between; align-items: center;">
            <div>
              <div style="font-weight: 700; font-size: 13px;">Certificate in TALLY Prime & GST</div>
              <div style="font-size: 11px; color: var(--neon-cyan);">AI08 • 6 Months • Accounting Lab</div>
            </div>
            <div style="text-align: right;">
              <div style="font-weight: 800; color: #FFD166; font-size: 14px;">₹1,299</div>
              <button data-apply-course="Certificate in TALLY Prime & GST" class="btn btn-primary" style="min-height: 28px; padding: 2px 8px; font-size: 11px; margin-top: 4px;">Apply</button>
            </div>
          </div>

          <!-- Course Card 4 -->
          <div class="card" style="background: var(--navy-surface); margin-bottom: 0; padding: 14px; display: flex; justify-content: space-between; align-items: center;">
            <div>
              <div style="font-weight: 700; font-size: 13px;">Certificate in Electrician</div>
              <div style="font-size: 11px; color: var(--neon-cyan);">AI91 • 6 Months • Technical & Engineering</div>
            </div>
            <div style="text-align: right;">
              <div style="font-weight: 800; color: #FFD166; font-size: 14px;">₹1,599</div>
              <button data-apply-course="Certificate in Electrician" class="btn btn-primary" style="min-height: 28px; padding: 2px 8px; font-size: 11px; margin-top: 4px;">Apply</button>
            </div>
          </div>

          <!-- Course Card 5 -->
          <div class="card" style="background: var(--navy-surface); margin-bottom: 0; padding: 14px; display: flex; justify-content: space-between; align-items: center;">
            <div>
              <div style="font-weight: 700; font-size: 13px;">Montessori Teacher Training (NTT)</div>
              <div style="font-size: 11px; color: var(--neon-cyan);">AI238 • 12 Months • Teaching & Sports</div>
            </div>
            <div style="text-align: right;">
              <div style="font-weight: 800; color: #FFD166; font-size: 14px;">₹2,000</div>
              <button data-apply-course="Certificate in Montessori Teacher Training" class="btn btn-primary" style="min-height: 28px; padding: 2px 8px; font-size: 11px; margin-top: 4px;">Apply</button>
            </div>
          </div>

          <!-- Course Card 6 -->
          <div class="card" style="background: var(--navy-surface); margin-bottom: 0; padding: 14px; display: flex; justify-content: space-between; align-items: center;">
            <div>
              <div style="font-weight: 700; font-size: 13px;">Early Childhood Care & Education (ECCE)</div>
              <div style="font-size: 11px; color: var(--neon-cyan);">AI225 • 24 Months • Master Diploma</div>
            </div>
            <div style="text-align: right;">
              <div style="font-weight: 800; color: #FFD166; font-size: 14px;">₹3,500</div>
              <button data-apply-course="Early Childhood Care & Education (ECCE)" class="btn btn-primary" style="min-height: 28px; padding: 2px 8px; font-size: 11px; margin-top: 4px;">Apply</button>
            </div>
          </div>
        </div>
      </section>

      <!-- Latest Notice Board -->
      <section class="card">
        <div class="card-title orange">📢 NOTICE BOARD & ANNOUNCEMENTS</div>
        <div style="display: flex; flex-direction: column; gap: 8px;">
          <div style="background: var(--navy-surface); border-left: 3px solid var(--neon-cyan); border-radius: 8px; padding: 10px 14px;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span class="badge badge-cyan" style="font-size: 10px;">ADMISSIONS 2026</span>
              <span style="font-size: 11px; color: var(--text-muted);">Active Now</span>
            </div>
            <div style="font-weight: 700; font-size: 13px; margin-top: 4px;">New Batches for ADCA, Tally Prime & Computer Diplomas</div>
            <div style="font-size: 11px; color: var(--text-secondary); margin-top: 2px;">Morning (8-10 AM) and Evening (4-6 PM) batches starting next Monday. Reserve your seat online today.</div>
          </div>

          <div style="background: var(--navy-surface); border-left: 3px solid var(--orange-accent); border-radius: 8px; padding: 10px 14px;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span class="badge badge-orange" style="font-size: 10px;">EXAM & CERTIFICATES</span>
              <span style="font-size: 11px; color: var(--text-muted);">Recent</span>
            </div>
            <div style="font-weight: 700; font-size: 13px; margin-top: 4px;">Practical Exam & ISO Certified Marksheet Distribution</div>
            <div style="font-size: 11px; color: var(--text-secondary); margin-top: 2px;">Enrolled students can verify status and view official credentials in the Student Portal.</div>
          </div>
        </div>
      </section>
    </div>
  `;
}
