import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { ValueComponent } from './value.component';
import { ValueDetailComponent } from './value-detail.component';
import { ValueUpdateComponent } from './value-update.component';
import { ValueDeleteDialogComponent } from './value-delete-dialog.component';
import { valueRoute } from './value.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(valueRoute)],
  declarations: [ValueComponent, ValueDetailComponent, ValueUpdateComponent, ValueDeleteDialogComponent],
  entryComponents: [ValueDeleteDialogComponent],
})
export class JhipsterSampleApplicationValueModule {}
