export function renderAboutContactScreen() {
  return `
    <div class="screen-container">
      <div style="margin-bottom: 16px;">
        <h2 style="font-family: var(--font-heading); font-size: 20px; font-weight: 800; color: var(--white-pure);">
          About & Contact Institute
        </h2>
        <div style="font-size: 12px; color: var(--neon-cyan);">
          Official Campus Details, Support Helpline & Locations
        </div>
      </div>

      <!-- Institute Identity Card -->
      <section class="card" style="text-align: center; padding: 24px 16px;">
        <div style="width: 80px; height: 80px; border-radius: 50%; background: #ffffff; border: 3px solid var(--orange-accent); margin: 0 auto 12px; padding: 3px; display: flex; align-items: center; justify-content: center; box-shadow: 0 0 16px rgba(255, 107, 0, 0.35);">
          <img src="/logo.png" alt="Srishti Computer Institute Logo" style="width: 100%; height: 100%; object-fit: contain; border-radius: 50%;" />
        </div>
        <h3 style="font-family: var(--font-heading); font-size: 18px; font-weight: 800; margin-bottom: 4px;">
          SRISHTI COMPUTER INSTITUTE
        </h3>
        <div class="badge badge-orange" style="margin-bottom: 14px;">
          LEARN • PRACTICE • GROW
        </div>
        <p style="font-size: 13px; color: var(--text-secondary); max-width: 520px; margin: 0 auto; line-height: 1.6;">
          Leading Computer & Vocational Education Institute in Mandhana, Kanpur Nagar. Providing industry-standard technical training, computer literacy programs, accounting, hardware and specialized job-oriented diploma programs.
        </p>
      </section>

      <!-- Primary Contact Quick Actions -->
      <div class="actions-grid">
        <a href="tel:8953994687" class="btn btn-cyan">
          📞 Call Now
        </a>
        <a href="https://wa.me/918953994687" target="_blank" rel="noopener noreferrer" class="btn btn-success">
          💬 WhatsApp
        </a>
        <a href="https://sites.google.com/view/srishti-computer-institute/home" target="_blank" rel="noopener noreferrer" class="btn btn-secondary">
          🌐 Website
        </a>
        <a href="https://share.google/tSYX6LZlGeJInp8cv" target="_blank" rel="noopener noreferrer" class="btn btn-primary">
          📍 Maps
        </a>
      </div>

      <!-- Direct Phone & WhatsApp Numbers -->
      <section class="card">
        <div class="card-title orange">DIRECT CONTACT NUMBERS</div>
        
        <div style="display: flex; align-items: center; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid var(--navy-border);">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 40px; height: 40px; border-radius: 10px; background: var(--navy-surface); display: flex; align-items: center; justify-content: center; font-size: 18px;">📱</div>
            <div>
              <div style="font-size: 11px; color: var(--text-muted);">Mobile Number</div>
              <div style="font-size: 15px; font-weight: 700; color: var(--white-pure);">8953994687</div>
            </div>
          </div>
          <a href="tel:8953994687" class="btn btn-cyan" style="min-height: 34px; padding: 4px 14px; font-size: 12px;">Call</a>
        </div>

        <div style="display: flex; align-items: center; justify-content: space-between; padding: 10px 0;">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 40px; height: 40px; border-radius: 10px; background: var(--navy-surface); display: flex; align-items: center; justify-content: center; font-size: 18px;">💬</div>
            <div>
              <div style="font-size: 11px; color: var(--text-muted);">WhatsApp Number</div>
              <div style="font-size: 15px; font-weight: 700; color: var(--white-pure);">8953994687</div>
            </div>
          </div>
          <a href="https://wa.me/918953994687" target="_blank" rel="noopener noreferrer" class="btn btn-success" style="min-height: 34px; padding: 4px 14px; font-size: 12px;">Chat</a>
        </div>
      </section>

      <!-- Campus Location -->
      <section class="card">
        <div class="card-title cyan">INSTITUTE ADDRESS & CAMPUS LOCATION</div>
        
        <div style="background: var(--navy-surface); border: 1px solid var(--navy-border); border-radius: 12px; padding: 14px; margin-bottom: 12px;">
          <div style="font-size: 14px; font-weight: 600; color: var(--white-pure); line-height: 1.5;">
            📍 Bagdaudhi Kachhar Kothi, Mandhana, Kanpur Nagar, Uttar Pradesh, India
          </div>
          <div style="font-size: 12px; color: var(--text-muted); margin-top: 6px;">
            ⏰ Campus Hours: Monday – Saturday: 08:00 AM to 06:00 PM (Sunday Closed)
          </div>
        </div>

        <a href="https://share.google/tSYX6LZlGeJInp8cv" target="_blank" rel="noopener noreferrer" class="btn btn-primary btn-full">
          📍 View Institute on Google Maps
        </a>
      </section>

      <!-- Official Community Channels -->
      <section class="card">
        <div class="card-title orange">OFFICIAL COMMUNITY CHANNELS</div>
        
        <a href="https://chat.whatsapp.com/L9Z8gxRL7QX3dJD3EEMBqO?s=cl&p=a&mlu=4&ilr=4&iam=2" target="_blank" rel="noopener noreferrer" class="btn btn-secondary btn-full" style="margin-bottom: 10px; border-color: var(--success-green); justify-content: flex-start;">
          👥 Join WhatsApp Group (Daily Updates & Batch Notices)
        </a>

        <a href="https://t.me/SrishtiComputerInstitute" target="_blank" rel="noopener noreferrer" class="btn btn-secondary btn-full" style="border-color: #0088CC; justify-content: flex-start;">
          ✈️ Join Telegram Channel (Study Materials & Quizzes)
        </a>
      </section>
    </div>
  `;
}
