import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IValues, Values } from 'app/shared/model/values.model';
import { ValuesService } from './values.service';
import { ITemperature } from 'app/shared/model/temperature.model';
import { TemperatureService } from 'app/entities/temperature/temperature.service';

@Component({
  selector: 'jhi-values-update',
  templateUrl: './values-update.component.html',
})
export class ValuesUpdateComponent implements OnInit {
  isSaving = false;
  temperatures: ITemperature[] = [];

  editForm = this.fb.group({
    id: [],
    value: [],
    timestamp: [],
    temperature: [],
    temperatures: [],
  });

  constructor(
    protected valuesService: ValuesService,
    protected temperatureService: TemperatureService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ values }) => {
      if (!values.id) {
        const today = moment().startOf('day');
        values.timestamp = today;
      }

      this.updateForm(values);

      this.temperatureService.query().subscribe((res: HttpResponse<ITemperature[]>) => (this.temperatures = res.body || []));
    });
  }

  updateForm(values: IValues): void {
    this.editForm.patchValue({
      id: values.id,
      value: values.value,
      timestamp: values.timestamp ? values.timestamp.format(DATE_TIME_FORMAT) : null,
      temperature: values.temperature,
      temperatures: values.temperatures,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const values = this.createFromForm();
    if (values.id !== undefined) {
      this.subscribeToSaveResponse(this.valuesService.update(values));
    } else {
      this.subscribeToSaveResponse(this.valuesService.create(values));
    }
  }

  private createFromForm(): IValues {
    return {
      ...new Values(),
      id: this.editForm.get(['id'])!.value,
      value: this.editForm.get(['value'])!.value,
      timestamp: this.editForm.get(['timestamp'])!.value ? moment(this.editForm.get(['timestamp'])!.value, DATE_TIME_FORMAT) : undefined,
      temperature: this.editForm.get(['temperature'])!.value,
      temperatures: this.editForm.get(['temperatures'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IValues>>): void {
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

  trackById(index: number, item: ITemperature): any {
    return item.id;
  }

  getSelected(selectedVals: ITemperature[], option: ITemperature): ITemperature {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
