import { Moment } from 'moment';

export interface IApplication {
  id?: number;
  fileName?: string;
  dataContentType?: string;
  data?: any;
  version?: string;
  timestamp?: Moment;
}

export class Application implements IApplication {
  constructor(
    public id?: number,
    public fileName?: string,
    public dataContentType?: string,
    public data?: any,
    public version?: string,
    public timestamp?: Moment
  ) {}
}
