import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './job-execution-report.reducer';

export const JobExecutionReport = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const jobExecutionReportList = useAppSelector(state => state.jobExecutionReport.entities);
  const loading = useAppSelector(state => state.jobExecutionReport.loading);
  const totalItems = useAppSelector(state => state.jobExecutionReport.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="job-execution-report-heading" data-cy="JobExecutionReportHeading">
        <Translate contentKey="cyberClinicApp.jobExecutionReport.home.title">Job Execution Reports</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cyberClinicApp.jobExecutionReport.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/job-execution-report/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cyberClinicApp.jobExecutionReport.home.createLabel">Create new Job Execution Report</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {jobExecutionReportList && jobExecutionReportList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cyberClinicApp.jobExecutionReport.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startTime')}>
                  <Translate contentKey="cyberClinicApp.jobExecutionReport.startTime">Start Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startTime')} />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  <Translate contentKey="cyberClinicApp.jobExecutionReport.endTime">End Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endTime')} />
                </th>
                <th className="hand" onClick={sort('executionNode')}>
                  <Translate contentKey="cyberClinicApp.jobExecutionReport.executionNode">Execution Node</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('executionNode')} />
                </th>
                <th className="hand" onClick={sort('executionLog')}>
                  <Translate contentKey="cyberClinicApp.jobExecutionReport.executionLog">Execution Log</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('executionLog')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="cyberClinicApp.jobExecutionReport.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th>
                  <Translate contentKey="cyberClinicApp.jobExecutionReport.jobRequest">Job Request</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cyberClinicApp.jobExecutionReport.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {jobExecutionReportList.map((jobExecutionReport, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/job-execution-report/${jobExecutionReport.id}`} color="link" size="sm">
                      {jobExecutionReport.id}
                    </Button>
                  </td>
                  <td>
                    {jobExecutionReport.startTime ? (
                      <TextFormat type="date" value={jobExecutionReport.startTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {jobExecutionReport.endTime ? (
                      <TextFormat type="date" value={jobExecutionReport.endTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{jobExecutionReport.executionNode}</td>
                  <td>{jobExecutionReport.executionLog}</td>
                  <td>
                    <Translate contentKey={`cyberClinicApp.Status.${jobExecutionReport.status}`} />
                  </td>
                  <td>
                    {jobExecutionReport.jobRequest ? (
                      <Link to={`/job-request/${jobExecutionReport.jobRequest.id}`}>{jobExecutionReport.jobRequest.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{jobExecutionReport.user ? jobExecutionReport.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/job-execution-report/${jobExecutionReport.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/job-execution-report/${jobExecutionReport.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/job-execution-report/${jobExecutionReport.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="cyberClinicApp.jobExecutionReport.home.notFound">No Job Execution Reports found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={jobExecutionReportList && jobExecutionReportList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default JobExecutionReport;
