import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPlace, Place } from 'app/shared/model/place.model';
import { PlaceService } from './place.service';
import { PlaceComponent } from './place.component';
import { PlaceDetailComponent } from './place-detail.component';
import { PlaceUpdateComponent } from './place-update.component';

@Injectable({ providedIn: 'root' })
export class PlaceResolve implements Resolve<IPlace> {
  constructor(private service: PlaceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlace> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((place: HttpResponse<Place>) => {
          if (place.body) {
            return of(place.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Place());
  }
}

export const placeRoute: Routes = [
  {
    path: '',
    component: PlaceComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.place.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlaceDetailComponent,
    resolve: {
      place: PlaceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.place.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlaceUpdateComponent,
    resolve: {
      place: PlaceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.place.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlaceUpdateComponent,
    resolve: {
      place: PlaceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.place.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
