import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './sensor.reducer';
import { ISensor } from 'app/shared/model/sensor.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISensorUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SensorUpdate = (props: ISensorUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { sensorEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/sensor' + props.location.search);
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
        ...sensorEntity,
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
          <h2 id="sensorsApp.sensor.home.createOrEditLabel" data-cy="SensorCreateUpdateHeading">
            <Translate contentKey="sensorsApp.sensor.home.createOrEditLabel">Create or edit a Sensor</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : sensorEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="sensor-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="sensor-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="variableLabel" for="sensor-variable">
                  <Translate contentKey="sensorsApp.sensor.variable">Variable</Translate>
                </Label>
                <AvField id="sensor-variable" data-cy="variable" type="text" name="variable" />
              </AvGroup>
              <AvGroup>
                <Label id="unitLabel" for="sensor-unit">
                  <Translate contentKey="sensorsApp.sensor.unit">Unit</Translate>
                </Label>
                <AvField id="sensor-unit" data-cy="unit" type="text" name="unit" />
              </AvGroup>
              <AvGroup>
                <Label id="valueLabel" for="sensor-value">
                  <Translate contentKey="sensorsApp.sensor.value">Value</Translate>
                </Label>
                <AvField id="sensor-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/sensor" replace color="info">
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
  sensorEntity: storeState.sensor.entity,
  loading: storeState.sensor.loading,
  updating: storeState.sensor.updating,
  updateSuccess: storeState.sensor.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SensorUpdate);
