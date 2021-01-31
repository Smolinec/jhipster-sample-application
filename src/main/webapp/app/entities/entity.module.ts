import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'place',
        loadChildren: () => import('./place/place.module').then(m => m.JhipsterSampleApplicationPlaceModule),
      },
      {
        path: 'device',
        loadChildren: () => import('./device/device.module').then(m => m.JhipsterSampleApplicationDeviceModule),
      },
      {
        path: 'temperature',
        loadChildren: () => import('./temperature/temperature.module').then(m => m.JhipsterSampleApplicationTemperatureModule),
      },
      {
        path: 'value',
        loadChildren: () => import('./value/value.module').then(m => m.JhipsterSampleApplicationValueModule),
      },
      {
        path: 'web-user',
        loadChildren: () => import('./web-user/web-user.module').then(m => m.JhipsterSampleApplicationWebUserModule),
      },
      {
        path: 'push-notification-token',
        loadChildren: () =>
          import('./push-notification-token/push-notification-token.module').then(
            m => m.JhipsterSampleApplicationPushNotificationTokenModule
          ),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class JhipsterSampleApplicationEntityModule {}
