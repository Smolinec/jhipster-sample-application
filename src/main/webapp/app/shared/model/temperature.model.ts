import { Moment } from 'moment';
import { IDevice } from 'app/shared/model/device.model';
import { IValue } from 'app/shared/model/value.model';

export interface ITemperature {
  id?: number;
  name?: string;
  address?: string;
  createTimestamp?: Moment;
  lastUpdateTimestamp?: Moment;
  device?: IDevice;
  values?: IValue[];
}

export class Temperature implements ITemperature {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string,
    public createTimestamp?: Moment,
    public lastUpdateTimestamp?: Moment,
    public device?: IDevice,
    public values?: IValue[]
  ) {}
}
