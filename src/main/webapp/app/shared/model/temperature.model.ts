import { Moment } from 'moment';
import { IDevice } from 'app/shared/model/device.model';
import { IValues } from 'app/shared/model/values.model';

export interface ITemperature {
  id?: number;
  name?: string;
  address?: string;
  createTimestamp?: Moment;
  lastUpdateTimestamp?: Moment;
  device?: IDevice;
  values?: IValues[];
}

export class Temperature implements ITemperature {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string,
    public createTimestamp?: Moment,
    public lastUpdateTimestamp?: Moment,
    public device?: IDevice,
    public values?: IValues[]
  ) {}
}
