import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import JobReport from './job-report';
import JobReportDetail from './job-report-detail';
import JobReportUpdate from './job-report-update';
import JobReportDeleteDialog from './job-report-delete-dialog';

const JobReportRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<JobReport />} />
    <Route path="new" element={<JobReportUpdate />} />
    <Route path=":id">
      <Route index element={<JobReportDetail />} />
      <Route path="edit" element={<JobReportUpdate />} />
      <Route path="delete" element={<JobReportDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default JobReportRoutes;
