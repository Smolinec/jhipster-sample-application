import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { ISMSNotification } from 'app/shared/model/sms-notification.model';

type EntityResponseType = HttpResponse<ISMSNotification>;
type EntityArrayResponseType = HttpResponse<ISMSNotification[]>;

@Injectable({ providedIn: 'root' })
export class SMSNotificationService {
  public resourceUrl = SERVER_API_URL + 'api/sms-notifications';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/sms-notifications';

  constructor(protected http: HttpClient) {}

  create(sMSNotification: ISMSNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sMSNotification);
    return this.http
      .post<ISMSNotification>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sMSNotification: ISMSNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sMSNotification);
    return this.http
      .put<ISMSNotification>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISMSNotification>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISMSNotification[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISMSNotification[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(sMSNotification: ISMSNotification): ISMSNotification {
    const copy: ISMSNotification = Object.assign({}, sMSNotification, {
      createdTimestamp:
        sMSNotification.createdTimestamp && sMSNotification.createdTimestamp.isValid()
          ? sMSNotification.createdTimestamp.toJSON()
          : undefined,
      sendingTimestamp:
        sMSNotification.sendingTimestamp && sMSNotification.sendingTimestamp.isValid()
          ? sMSNotification.sendingTimestamp.toJSON()
          : undefined,
      sendTimestamp:
        sMSNotification.sendTimestamp && sMSNotification.sendTimestamp.isValid() ? sMSNotification.sendTimestamp.toJSON() : undefined,
      featureSend: sMSNotification.featureSend && sMSNotification.featureSend.isValid() ? sMSNotification.featureSend.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdTimestamp = res.body.createdTimestamp ? moment(res.body.createdTimestamp) : undefined;
      res.body.sendingTimestamp = res.body.sendingTimestamp ? moment(res.body.sendingTimestamp) : undefined;
      res.body.sendTimestamp = res.body.sendTimestamp ? moment(res.body.sendTimestamp) : undefined;
      res.body.featureSend = res.body.featureSend ? moment(res.body.featureSend) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sMSNotification: ISMSNotification) => {
        sMSNotification.createdTimestamp = sMSNotification.createdTimestamp ? moment(sMSNotification.createdTimestamp) : undefined;
        sMSNotification.sendingTimestamp = sMSNotification.sendingTimestamp ? moment(sMSNotification.sendingTimestamp) : undefined;
        sMSNotification.sendTimestamp = sMSNotification.sendTimestamp ? moment(sMSNotification.sendTimestamp) : undefined;
        sMSNotification.featureSend = sMSNotification.featureSend ? moment(sMSNotification.featureSend) : undefined;
      });
    }
    return res;
  }
}
