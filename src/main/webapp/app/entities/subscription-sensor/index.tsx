import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionSensor from './subscription-sensor';
import SubscriptionSensorDetail from './subscription-sensor-detail';
import SubscriptionSensorUpdate from './subscription-sensor-update';
import SubscriptionSensorDeleteDialog from './subscription-sensor-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionSensorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionSensorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionSensorDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionSensor} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubscriptionSensorDeleteDialog} />
  </>
);

export default Routes;
