import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import JobExecutionReport from './job-execution-report';
import JobExecutionReportDetail from './job-execution-report-detail';
import JobExecutionReportUpdate from './job-execution-report-update';
import JobExecutionReportDeleteDialog from './job-execution-report-delete-dialog';

const JobExecutionReportRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<JobExecutionReport />} />
    <Route path="new" element={<JobExecutionReportUpdate />} />
    <Route path=":id">
      <Route index element={<JobExecutionReportDetail />} />
      <Route path="edit" element={<JobExecutionReportUpdate />} />
      <Route path="delete" element={<JobExecutionReportDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default JobExecutionReportRoutes;
