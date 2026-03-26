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

import { getEntities } from './sanitization-report.reducer';

export const SanitizationReport = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const sanitizationReportList = useAppSelector(state => state.sanitizationReport.entities);
  const loading = useAppSelector(state => state.sanitizationReport.loading);
  const totalItems = useAppSelector(state => state.sanitizationReport.totalItems);
  const account = useAppSelector(state => state.authentication.account);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        id: account?.id,
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
      <h2 id="sanitization-report-heading" data-cy="SanitizationReportHeading">
        <Translate contentKey="cyberClinicApp.sanitizationReport.home.title">Sanitization Reports</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cyberClinicApp.sanitizationReport.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/sanitization-report/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cyberClinicApp.sanitizationReport.home.createLabel">Create new Sanitization Report</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {sanitizationReportList && sanitizationReportList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cyberClinicApp.sanitizationReport.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('reportDate')}>
                  <Translate contentKey="cyberClinicApp.sanitizationReport.reportDate">Report Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reportDate')} />
                </th>
                <th className="hand" onClick={sort('details')}>
                  <Translate contentKey="cyberClinicApp.sanitizationReport.details">Details</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('details')} />
                </th>
                <th className="hand" onClick={sort('isSuccessful')}>
                  <Translate contentKey="cyberClinicApp.sanitizationReport.isSuccessful">Is Successful</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isSuccessful')} />
                </th>
                <th>
                  <Translate contentKey="cyberClinicApp.sanitizationReport.jobRequest">Job Request</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cyberClinicApp.sanitizationReport.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {sanitizationReportList.map((sanitizationReport, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/sanitization-report/${sanitizationReport.id}`} color="link" size="sm">
                      {sanitizationReport.id}
                    </Button>
                  </td>
                  <td>
                    {sanitizationReport.reportDate ? (
                      <TextFormat type="date" value={sanitizationReport.reportDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{sanitizationReport.details}</td>
                  <td>{sanitizationReport.isSuccessful ? 'true' : 'false'}</td>
                  <td>
                    {sanitizationReport.jobRequest ? (
                      <Link to={`/job-request/${sanitizationReport.jobRequest.id}`}>{sanitizationReport.jobRequest.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{sanitizationReport.user ? sanitizationReport.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        onClick={() =>
                          (window.location.href = `/sanitization-report/${sanitizationReport.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="cyberClinicApp.sanitizationReport.home.notFound">No Sanitization Reports found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={sanitizationReportList && sanitizationReportList.length > 0 ? '' : 'd-none'}>
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

export default SanitizationReport;
