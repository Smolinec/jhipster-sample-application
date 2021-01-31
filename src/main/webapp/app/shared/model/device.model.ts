import { IPlace } from 'app/shared/model/place.model';

export interface IDevice {
  id?: number;
  uuid?: string;
  appVersion?: string;
  idUpdated?: boolean;
  place?: IPlace;
}

export class Device implements IDevice {
  constructor(public id?: number, public uuid?: string, public appVersion?: string, public idUpdated?: boolean, public place?: IPlace) {
    this.idUpdated = this.idUpdated || false;
  }
}
