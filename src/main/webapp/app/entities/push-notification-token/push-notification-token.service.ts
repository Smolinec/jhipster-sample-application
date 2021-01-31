import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IPushNotificationToken } from 'app/shared/model/push-notification-token.model';

type EntityResponseType = HttpResponse<IPushNotificationToken>;
type EntityArrayResponseType = HttpResponse<IPushNotificationToken[]>;

@Injectable({ providedIn: 'root' })
export class PushNotificationTokenService {
  public resourceUrl = SERVER_API_URL + 'api/push-notification-tokens';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/push-notification-tokens';

  constructor(protected http: HttpClient) {}

  create(pushNotificationToken: IPushNotificationToken): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pushNotificationToken);
    return this.http
      .post<IPushNotificationToken>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pushNotificationToken: IPushNotificationToken): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pushNotificationToken);
    return this.http
      .put<IPushNotificationToken>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPushNotificationToken>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPushNotificationToken[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPushNotificationToken[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(pushNotificationToken: IPushNotificationToken): IPushNotificationToken {
    const copy: IPushNotificationToken = Object.assign({}, pushNotificationToken, {
      timestamp:
        pushNotificationToken.timestamp && pushNotificationToken.timestamp.isValid() ? pushNotificationToken.timestamp.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timestamp = res.body.timestamp ? moment(res.body.timestamp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pushNotificationToken: IPushNotificationToken) => {
        pushNotificationToken.timestamp = pushNotificationToken.timestamp ? moment(pushNotificationToken.timestamp) : undefined;
      });
    }
    return res;
  }
}
