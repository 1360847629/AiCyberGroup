import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import JobRequest from './job-request';
import JobReport from './job-report';
import JobExecutionReport from './job-execution-report';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="job-request/*" element={<JobRequest />} />
        <Route path="job-report/*" element={<JobReport />} />
        <Route path="job-execution-report/*" element={<JobExecutionReport />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
