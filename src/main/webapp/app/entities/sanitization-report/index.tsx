import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SanitizationReport from './sanitization-report';
import SanitizationReportDetail from './sanitization-report-detail';
import SanitizationReportUpdate from './sanitization-report-update';
import SanitizationReportDeleteDialog from './sanitization-report-delete-dialog';

const SanitizationReportRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SanitizationReport />} />
    <Route path="new" element={<SanitizationReportUpdate />} />
    <Route path=":id">
      <Route index element={<SanitizationReportDetail />} />
      <Route path="edit" element={<SanitizationReportUpdate />} />
      <Route path="delete" element={<SanitizationReportDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SanitizationReportRoutes;
