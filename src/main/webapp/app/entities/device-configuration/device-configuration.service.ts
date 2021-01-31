import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IDeviceConfiguration } from 'app/shared/model/device-configuration.model';

type EntityResponseType = HttpResponse<IDeviceConfiguration>;
type EntityArrayResponseType = HttpResponse<IDeviceConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class DeviceConfigurationService {
  public resourceUrl = SERVER_API_URL + 'api/device-configurations';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/device-configurations';

  constructor(protected http: HttpClient) {}

  create(deviceConfiguration: IDeviceConfiguration): Observable<EntityResponseType> {
    return this.http.post<IDeviceConfiguration>(this.resourceUrl, deviceConfiguration, { observe: 'response' });
  }

  update(deviceConfiguration: IDeviceConfiguration): Observable<EntityResponseType> {
    return this.http.put<IDeviceConfiguration>(this.resourceUrl, deviceConfiguration, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeviceConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeviceConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeviceConfiguration[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
