import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IApplication, Application } from 'app/shared/model/application.model';
import { ApplicationService } from './application.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-application-update',
  templateUrl: './application-update.component.html',
})
export class ApplicationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fileName: [],
    data: [],
    dataContentType: [],
    version: [],
    timestamp: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected applicationService: ApplicationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ application }) => {
      if (!application.id) {
        const today = moment().startOf('day');
        application.timestamp = today;
      }

      this.updateForm(application);
    });
  }

  updateForm(application: IApplication): void {
    this.editForm.patchValue({
      id: application.id,
      fileName: application.fileName,
      data: application.data,
      dataContentType: application.dataContentType,
      version: application.version,
      timestamp: application.timestamp ? application.timestamp.format(DATE_TIME_FORMAT) : null,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('jhipsterSampleApplicationApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const application = this.createFromForm();
    if (application.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationService.update(application));
    } else {
      this.subscribeToSaveResponse(this.applicationService.create(application));
    }
  }

  private createFromForm(): IApplication {
    return {
      ...new Application(),
      id: this.editForm.get(['id'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      dataContentType: this.editForm.get(['dataContentType'])!.value,
      data: this.editForm.get(['data'])!.value,
      version: this.editForm.get(['version'])!.value,
      timestamp: this.editForm.get(['timestamp'])!.value ? moment(this.editForm.get(['timestamp'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplication>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
