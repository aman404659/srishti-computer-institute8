export function renderCourseModal(course) {
  if (!course) return '';

  return `
    <div class="modal-overlay" id="course-modal-overlay">
      <div class="modal-sheet">
        <div class="modal-header">
          <div>
            <span class="badge badge-cyan" style="font-size: 10px;">${course.code}</span>
            <span class="badge badge-orange" style="font-size: 10px; margin-left: 4px;">${course.category}</span>
          </div>
          <button class="modal-close-btn" id="modal-close-btn">&times;</button>
        </div>

        <h3 style="font-family: var(--font-heading); font-size: 18px; font-weight: 800; color: var(--white-pure); margin-bottom: 2px;">
          ${course.nameEn}
        </h3>
        <div style="font-size: 13px; color: var(--text-muted); margin-bottom: 16px;">
          ${course.nameHi}
        </div>

        <div style="background: var(--navy-surface); border: 1.5px solid var(--navy-border); border-radius: 12px; padding: 14px; margin-bottom: 16px;">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
            <span style="font-size: 12px; color: var(--text-muted);">Standard Course Fee:</span>
            <span style="font-size: 20px; font-weight: 800; color: #FFD166;">₹${course.fee.toLocaleString('en-IN')}</span>
          </div>
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 8px; font-size: 12px; border-top: 1px solid var(--navy-border); padding-top: 8px;">
            <div><span style="color: var(--text-muted);">Duration:</span> <strong>${course.durationEn}</strong></div>
            <div><span style="color: var(--text-muted);">Eligibility:</span> <strong>${course.eligibilityEn}</strong></div>
          </div>
        </div>

        <div style="margin-bottom: 16px;">
          <h4 style="font-size: 13px; font-weight: 700; color: var(--neon-cyan); margin-bottom: 8px; text-transform: uppercase; letter-spacing: 1px;">
            Syllabus & Practical Modules
          </h4>
          <div style="background: var(--navy-surface); border-radius: 10px; padding: 12px; font-size: 13px; color: var(--text-secondary); line-height: 1.6;">
            ${course.syllabusSummary.split(',').map(topic => `
              <div style="display: flex; align-items: flex-start; gap: 6px; margin-bottom: 4px;">
                <span style="color: var(--orange-accent);">✓</span>
                <span>${topic.trim()}</span>
              </div>
            `).join('')}
          </div>
        </div>

        <div style="font-size: 11px; color: var(--text-muted); margin-bottom: 16px;">
          🎓 Includes Official Courseware, Practical Lab Training, Periodic Assessments, and Govt. Registered Institute Certificate upon completion.
        </div>

        <div style="display: flex; gap: 10px;">
          <button id="modal-apply-btn" data-apply-course="${course.nameEn}" class="btn btn-primary" style="flex: 1;">
            ✍️ Apply for this Course
          </button>
          <a href="https://wa.me/918953994687?text=${encodeURIComponent(`Hello Srishti Institute, I am interested in ${course.nameEn} (${course.code}). Please share more details.`)}" target="_blank" rel="noopener noreferrer" class="btn btn-success" style="padding: 12px 14px;">
            💬 Ask on WhatsApp
          </a>
        </div>
      </div>
    </div>
  `;
}
