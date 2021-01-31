import { Moment } from 'moment';
import { AlertType } from 'app/shared/model/enumerations/alert-type.model';

export interface ISMSNotification {
  id?: number;
  telNumber?: string;
  message?: string;
  createdTimestamp?: Moment;
  uuidDevice?: string;
  isSending?: boolean;
  sendingTimestamp?: Moment;
  isSend?: boolean;
  sendTimestamp?: Moment;
  alertType?: AlertType;
  featureSend?: Moment;
}

export class SMSNotification implements ISMSNotification {
  constructor(
    public id?: number,
    public telNumber?: string,
    public message?: string,
    public createdTimestamp?: Moment,
    public uuidDevice?: string,
    public isSending?: boolean,
    public sendingTimestamp?: Moment,
    public isSend?: boolean,
    public sendTimestamp?: Moment,
    public alertType?: AlertType,
    public featureSend?: Moment
  ) {
    this.isSending = this.isSending || false;
    this.isSend = this.isSend || false;
  }
}
