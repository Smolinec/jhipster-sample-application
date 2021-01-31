import { IPlace } from 'app/shared/model/place.model';
import { IDeviceProfile } from 'app/shared/model/device-profile.model';

export interface IDevice {
  id?: number;
  uuid?: string;
  appVersion?: string;
  idUpdated?: boolean;
  place?: IPlace;
  deviceProfiles?: IDeviceProfile[];
}

export class Device implements IDevice {
  constructor(
    public id?: number,
    public uuid?: string,
    public appVersion?: string,
    public idUpdated?: boolean,
    public place?: IPlace,
    public deviceProfiles?: IDeviceProfile[]
  ) {
    this.idUpdated = this.idUpdated || false;
  }
}
