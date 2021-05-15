import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './subscription-sensor.reducer';
import { ISubscriptionSensor } from 'app/shared/model/subscription-sensor.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionSensorUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionSensorUpdate = (props: ISubscriptionSensorUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { subscriptionSensorEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/subscription-sensor');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...subscriptionSensorEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jayjayApplicationApp.subscriptionSensor.home.createOrEditLabel" data-cy="SubscriptionSensorCreateUpdateHeading">
            <Translate contentKey="jayjayApplicationApp.subscriptionSensor.home.createOrEditLabel">
              Create or edit a SubscriptionSensor
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : subscriptionSensorEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="subscription-sensor-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="subscription-sensor-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="telegramIdLabel" for="subscription-sensor-telegramId">
                  <Translate contentKey="jayjayApplicationApp.subscriptionSensor.telegramId">Telegram Id</Translate>
                </Label>
                <AvField
                  id="subscription-sensor-telegramId"
                  data-cy="telegramId"
                  type="string"
                  className="form-control"
                  name="telegramId"
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="subscription-sensor-name">
                  <Translate contentKey="jayjayApplicationApp.subscriptionSensor.name">Name</Translate>
                </Label>
                <AvField id="subscription-sensor-name" data-cy="name" type="text" name="name" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/subscription-sensor" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  subscriptionSensorEntity: storeState.subscriptionSensor.entity,
  loading: storeState.subscriptionSensor.loading,
  updating: storeState.subscriptionSensor.updating,
  updateSuccess: storeState.subscriptionSensor.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionSensorUpdate);
