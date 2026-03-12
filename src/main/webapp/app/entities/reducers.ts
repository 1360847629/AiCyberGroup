import jobRequest from 'app/entities/job-request/job-request.reducer';
import jobReport from 'app/entities/job-report/job-report.reducer';
import jobExecutionReport from 'app/entities/job-execution-report/job-execution-report.reducer';
import sanitizationReport from 'app/entities/sanitization-report/sanitization-report.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  jobRequest,
  jobReport,
  jobExecutionReport,
  sanitizationReport,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
