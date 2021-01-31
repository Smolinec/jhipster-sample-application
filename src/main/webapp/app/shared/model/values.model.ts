import { Moment } from 'moment';
import { ITemperature } from 'app/shared/model/temperature.model';

export interface IValues {
  id?: number;
  value?: number;
  timestamp?: Moment;
  temperature?: ITemperature;
  temperatures?: ITemperature[];
}

export class Values implements IValues {
  constructor(
    public id?: number,
    public value?: number,
    public timestamp?: Moment,
    public temperature?: ITemperature,
    public temperatures?: ITemperature[]
  ) {}
}
