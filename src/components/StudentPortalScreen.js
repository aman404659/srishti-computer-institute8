export function renderStudentPortalScreen() {
  return `
    <div class="screen-container">
      <div style="margin-bottom: 16px;">
        <h2 style="font-family: var(--font-heading); font-size: 20px; font-weight: 800; color: var(--white-pure);">
          Student Verification Portal
        </h2>
        <div style="font-size: 12px; color: var(--neon-cyan);">
          Check Admission Status, Enrollment Details & Official Institute Credentials
        </div>
      </div>

      <!-- Search Card -->
      <div class="card">
        <div class="card-title cyan">TRACK YOUR APPLICATION / ENROLLMENT</div>
        <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 14px;">
          Enter your Application Reference Number (e.g. <code>SCI-2026-XXXX</code>) or your registered 10-digit mobile number:
        </p>

        <form id="student-search-form" style="display: flex; gap: 8px;">
          <input 
            type="text" 
            id="student-search-input" 
            class="form-input" 
            placeholder="e.g. SCI-2026-1001 or 9876543210" 
            required 
            style="flex: 1;"
          />
          <button type="submit" class="btn btn-primary" style="white-space: nowrap;">
            🔍 Check Status
          </button>
        </form>
      </div>

      <!-- Results Area -->
      <div id="student-search-results">
        <!-- Rendered after search -->
      </div>
    </div>
  `;
}

export function renderStudentResultCard(app) {
  const isApproved = app.status === 'APPROVED';
  const isRejected = app.status === 'REJECTED';
  const statusBadge = isApproved 
    ? '<span class="badge badge-green">APPROVED / ENROLLED</span>'
    : isRejected 
    ? '<span class="badge" style="background: var(--danger-bg); color: var(--danger-red); border: 1px solid var(--danger-red);">REJECTED</span>'
    : '<span class="badge badge-amber">PENDING VERIFICATION</span>';

  return `
    <div class="card" style="border-color: ${isApproved ? 'var(--success-green)' : 'var(--navy-border)'}; background: var(--navy-midnight);">
      <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 14px; border-bottom: 1px solid var(--navy-border); padding-bottom: 10px;">
        <div>
          <div style="font-size: 11px; color: var(--text-muted);">Application Reference No.</div>
          <div style="font-family: var(--font-heading); font-size: 17px; font-weight: 800; color: var(--neon-cyan);">${app.referenceNo}</div>
        </div>
        <div>
          ${statusBadge}
        </div>
      </div>

      ${app.enrollmentNo ? `
        <div style="background: var(--cyan-container); border: 1.5px solid var(--neon-cyan); border-radius: 10px; padding: 12px; margin-bottom: 14px; text-align: center;">
          <div style="font-size: 11px; color: var(--text-muted); text-transform: uppercase; font-weight: 700;">Official Student Enrollment No.</div>
          <div style="font-size: 20px; font-weight: 800; color: var(--neon-cyan); letter-spacing: 1px; font-family: var(--font-heading);">${app.enrollmentNo}</div>
        </div>
      ` : ''}

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; font-size: 13px; margin-bottom: 14px;">
        <div>
          <span style="color: var(--text-muted); font-size: 11px; display: block;">Student Name</span>
          <strong>${app.fullName}</strong>
        </div>
        <div>
          <span style="color: var(--text-muted); font-size: 11px; display: block;">Father's Name</span>
          <strong>${app.fatherName || 'N/A'}</strong>
        </div>
        <div>
          <span style="color: var(--text-muted); font-size: 11px; display: block;">Registered Mobile</span>
          <strong>${app.phone}</strong>
        </div>
        <div>
          <span style="color: var(--text-muted); font-size: 11px; display: block;">Batch Timing</span>
          <strong>${app.batchTiming}</strong>
        </div>
        <div style="grid-column: span 2;">
          <span style="color: var(--text-muted); font-size: 11px; display: block;">Course</span>
          <strong style="color: #FFD166;">${app.courseName}</strong>
        </div>
      </div>

      <div style="background: var(--navy-surface); border-radius: 8px; padding: 10px 12px; margin-bottom: 14px; font-size: 12px;">
        <span style="color: var(--text-muted);">Institute Remarks:</span>
        <div style="color: var(--text-secondary); margin-top: 2px;">
          ${app.adminRemarks || 'Application under routine review by admissions department.'}
        </div>
      </div>

      <div style="display: flex; gap: 8px; flex-wrap: wrap;">
        <button onclick="window.print()" class="btn btn-secondary" style="flex: 1; font-size: 12px; min-height: 38px;">
          🖨️ Print Slip
        </button>
        <a href="tel:8953994687" class="btn btn-cyan" style="flex: 1; font-size: 12px; min-height: 38px;">
          📞 Helpline (8953994687)
        </a>
      </div>
    </div>
  `;
}
