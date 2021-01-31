import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IValue } from 'app/shared/model/value.model';

type EntityResponseType = HttpResponse<IValue>;
type EntityArrayResponseType = HttpResponse<IValue[]>;

@Injectable({ providedIn: 'root' })
export class ValueService {
  public resourceUrl = SERVER_API_URL + 'api/values';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/values';

  constructor(protected http: HttpClient) {}

  create(value: IValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(value);
    return this.http
      .post<IValue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(value: IValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(value);
    return this.http
      .put<IValue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IValue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IValue[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IValue[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(value: IValue): IValue {
    const copy: IValue = Object.assign({}, value, {
      timestamp: value.timestamp && value.timestamp.isValid() ? value.timestamp.toJSON() : undefined,
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
      res.body.forEach((value: IValue) => {
        value.timestamp = value.timestamp ? moment(value.timestamp) : undefined;
      });
    }
    return res;
  }
}
