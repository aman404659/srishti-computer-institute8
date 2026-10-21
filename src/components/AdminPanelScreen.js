import { getAdminToken } from '../data/store.js';

export function renderAdminPanelScreen({ isAuthenticated = false, applications = [], activeTab = 'ALL' } = {}) {
  if (!isAuthenticated) {
    return `
      <div class="screen-container" style="max-width: 480px; margin: 20px auto;">
        <div class="card" style="padding: 24px; text-align: center;">
          <div style="font-size: 40px; margin-bottom: 8px;">🔐</div>
          <h2 style="font-family: var(--font-heading); font-size: 20px; font-weight: 800; margin-bottom: 4px;">
            Institute Admin Portal
          </h2>
          <div style="font-size: 12px; color: var(--text-muted); margin-bottom: 20px;">
            Authorized Access for Institute Management Only
          </div>

          <div id="admin-login-error" class="alert alert-danger" style="display: none; font-size: 12px; text-align: left;"></div>

          <form id="admin-login-form" style="text-align: left;">
            <div class="form-group">
              <label class="form-label">Admin Username</label>
              <input type="text" id="admin-username" class="form-input" value="admin" autocomplete="username" />
            </div>

            <div class="form-group">
              <label class="form-label">Admin Password</label>
              <input type="password" id="admin-password" class="form-input" placeholder="Enter secure admin password" required autocomplete="current-password" />
            </div>

            <div style="font-size: 11px; color: var(--text-muted); margin-bottom: 14px;">
              🛡️ Credentials are validated server-side using cryptographic salted hash. Never exposed to browser clients.
            </div>

            <button type="submit" id="btn-admin-login" class="btn btn-primary btn-full">
              Sign In to Admin Dashboard
            </button>
          </form>
        </div>
      </div>
    `;
  }

  // Calculate statistics
  const total = applications.length;
  const pending = applications.filter(a => a.status === 'PENDING').length;
  const approved = applications.filter(a => a.status === 'APPROVED').length;
  const rejected = applications.filter(a => a.status === 'REJECTED').length;

  let filteredApps = applications;
  if (activeTab === 'PENDING') filteredApps = applications.filter(a => a.status === 'PENDING');
  if (activeTab === 'APPROVED') filteredApps = applications.filter(a => a.status === 'APPROVED');
  if (activeTab === 'REJECTED') filteredApps = applications.filter(a => a.status === 'REJECTED');

  return `
    <div class="screen-container">
      <!-- Admin Header Bar -->
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; flex-wrap: wrap; gap: 10px;">
        <div>
          <h2 style="font-family: var(--font-heading); font-size: 20px; font-weight: 800; color: var(--white-pure);">
            Institute Administration Dashboard
          </h2>
          <div style="font-size: 12px; color: var(--neon-cyan);">
            SRISHTI COMPUTER INSTITUTE • Real-Time Database Management
          </div>
        </div>
        <div style="display: flex; gap: 8px;">
          <button id="admin-export-btn" class="btn btn-secondary" style="min-height: 36px; padding: 6px 12px; font-size: 12px;">
            📥 Export CSV
          </button>
          <button id="admin-logout-btn" class="btn btn-cyan" style="min-height: 36px; padding: 6px 12px; font-size: 12px;">
            🚪 Sign Out
          </button>
        </div>
      </div>

      <!-- Stats Cards -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 10px; margin-bottom: 16px;">
        <div class="card" style="padding: 12px; text-align: center; margin-bottom: 0;">
          <div style="font-size: 20px; font-weight: 800; color: var(--neon-cyan);">${total}</div>
          <div style="font-size: 11px; color: var(--text-muted);">Total Inquiries</div>
        </div>
        <div class="card" style="padding: 12px; text-align: center; margin-bottom: 0;">
          <div style="font-size: 20px; font-weight: 800; color: var(--warning-amber);">${pending}</div>
          <div style="font-size: 11px; color: var(--text-muted);">Pending Review</div>
        </div>
        <div class="card" style="padding: 12px; text-align: center; margin-bottom: 0;">
          <div style="font-size: 20px; font-weight: 800; color: var(--success-green);">${approved}</div>
          <div style="font-size: 11px; color: var(--text-muted);">Enrolled / Approved</div>
        </div>
        <div class="card" style="padding: 12px; text-align: center; margin-bottom: 0;">
          <div style="font-size: 20px; font-weight: 800; color: var(--danger-red);">${rejected}</div>
          <div style="font-size: 11px; color: var(--text-muted);">Rejected</div>
        </div>
      </div>

      <!-- Filter Tabs -->
      <div style="display: flex; gap: 6px; margin-bottom: 14px; overflow-x: auto; padding-bottom: 4px;">
        <button data-admin-tab="ALL" class="btn ${activeTab === 'ALL' ? 'btn-primary' : 'btn-secondary'}" style="min-height: 34px; padding: 4px 12px; font-size: 12px;">
          All (${total})
        </button>
        <button data-admin-tab="PENDING" class="btn ${activeTab === 'PENDING' ? 'btn-primary' : 'btn-secondary'}" style="min-height: 34px; padding: 4px 12px; font-size: 12px;">
          Pending (${pending})
        </button>
        <button data-admin-tab="APPROVED" class="btn ${activeTab === 'APPROVED' ? 'btn-primary' : 'btn-secondary'}" style="min-height: 34px; padding: 4px 12px; font-size: 12px;">
          Approved (${approved})
        </button>
        <button data-admin-tab="REJECTED" class="btn ${activeTab === 'REJECTED' ? 'btn-primary' : 'btn-secondary'}" style="min-height: 34px; padding: 4px 12px; font-size: 12px;">
          Rejected (${rejected})
        </button>
      </div>

      <!-- Applications List -->
      <div style="display: flex; flex-direction: column; gap: 12px;">
        ${filteredApps.length === 0 ? `
          <div class="card" style="text-align: center; padding: 32px 16px;">
            <div style="font-size: 28px; margin-bottom: 6px;">📋</div>
            <div style="font-weight: 700;">No registrations in this category</div>
          </div>
        ` : filteredApps.map(app => {
          const isAppApproved = app.status === 'APPROVED';
          const isAppRejected = app.status === 'REJECTED';
          return `
            <div class="card" style="padding: 16px; margin-bottom: 0; background: var(--navy-midnight); border-color: ${isAppApproved ? 'var(--success-green)' : 'var(--navy-border)'};">
              <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px; flex-wrap: wrap; gap: 6px;">
                <div>
                  <span class="badge ${isAppApproved ? 'badge-green' : isAppRejected ? 'badge-amber' : 'badge-cyan'}">${app.referenceNo}</span>
                  ${app.enrollmentNo ? `<span class="badge badge-green" style="margin-left: 4px;">${app.enrollmentNo}</span>` : ''}
                  <h3 style="font-size: 16px; font-weight: 700; margin-top: 6px; color: var(--white-pure);">${app.fullName}</h3>
                </div>
                <div style="text-align: right;">
                  <span style="font-size: 11px; color: var(--text-muted);">${new Date(app.appliedAtMillis || Date.now()).toLocaleDateString('en-IN')}</span>
                  <div style="font-size: 14px; font-weight: 800; color: #FFD166;">₹${Number(app.fee || 1299).toLocaleString('en-IN')}</div>
                </div>
              </div>

              <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 8px; font-size: 12px; background: var(--navy-surface); padding: 10px 12px; border-radius: 8px; margin-bottom: 10px;">
                <div><span style="color: var(--text-muted);">Father's Name:</span> <strong>${app.fatherName || 'N/A'}</strong></div>
                <div><span style="color: var(--text-muted);">Mobile:</span> <a href="tel:${app.phone}" style="color: var(--neon-cyan); text-decoration: none; font-weight: 700;">${app.phone}</a></div>
                <div><span style="color: var(--text-muted);">Course:</span> <strong style="color: #FFD166;">${app.courseName}</strong></div>
                <div><span style="color: var(--text-muted);">Batch:</span> <strong>${app.batchTiming}</strong></div>
                <div style="grid-column: span 2;"><span style="color: var(--text-muted);">Address:</span> ${app.address}</div>
              </div>

              ${app.adminRemarks ? `
                <div style="font-size: 11px; color: var(--text-secondary); margin-bottom: 10px;">
                  <span style="color: var(--text-muted);">Remarks:</span> ${app.adminRemarks}
                </div>
              ` : ''}

              <div style="display: flex; gap: 8px; flex-wrap: wrap;">
                ${!isAppApproved ? `
                  <button data-admin-action="approve" data-app-id="${app.id || app.referenceNo}" class="btn btn-success" style="min-height: 32px; padding: 4px 10px; font-size: 12px;">
                    ✅ Approve & Enroll
                  </button>
                ` : ''}
                ${!isAppRejected ? `
                  <button data-admin-action="reject" data-app-id="${app.id || app.referenceNo}" class="btn btn-secondary" style="min-height: 32px; padding: 4px 10px; font-size: 12px; color: var(--danger-red); border-color: var(--danger-red);">
                    ❌ Reject
                  </button>
                ` : ''}
                <a href="https://wa.me/91${app.phone}?text=${encodeURIComponent(`Dear ${app.fullName}, regarding your Srishti Computer Institute admission application (${app.referenceNo})...`)}" target="_blank" rel="noopener noreferrer" class="btn btn-secondary" style="min-height: 32px; padding: 4px 10px; font-size: 12px;">
                  💬 WhatsApp Student
                </a>
              </div>
            </div>
          `;
        }).join('')}
      </div>
    </div>
  `;
}
