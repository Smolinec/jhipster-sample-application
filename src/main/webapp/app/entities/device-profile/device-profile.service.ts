import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IDeviceProfile } from 'app/shared/model/device-profile.model';

type EntityResponseType = HttpResponse<IDeviceProfile>;
type EntityArrayResponseType = HttpResponse<IDeviceProfile[]>;

@Injectable({ providedIn: 'root' })
export class DeviceProfileService {
  public resourceUrl = SERVER_API_URL + 'api/device-profiles';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/device-profiles';

  constructor(protected http: HttpClient) {}

  create(deviceProfile: IDeviceProfile): Observable<EntityResponseType> {
    return this.http.post<IDeviceProfile>(this.resourceUrl, deviceProfile, { observe: 'response' });
  }

  update(deviceProfile: IDeviceProfile): Observable<EntityResponseType> {
    return this.http.put<IDeviceProfile>(this.resourceUrl, deviceProfile, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeviceProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeviceProfile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeviceProfile[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
