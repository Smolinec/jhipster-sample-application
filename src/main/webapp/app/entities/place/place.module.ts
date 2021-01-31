import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { PlaceComponent } from './place.component';
import { PlaceDetailComponent } from './place-detail.component';
import { PlaceUpdateComponent } from './place-update.component';
import { PlaceDeleteDialogComponent } from './place-delete-dialog.component';
import { placeRoute } from './place.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(placeRoute)],
  declarations: [PlaceComponent, PlaceDetailComponent, PlaceUpdateComponent, PlaceDeleteDialogComponent],
  entryComponents: [PlaceDeleteDialogComponent],
})
export class JhipsterSampleApplicationPlaceModule {}
