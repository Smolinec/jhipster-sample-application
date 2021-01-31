import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IValue, Value } from 'app/shared/model/value.model';
import { ValueService } from './value.service';
import { ITemperature } from 'app/shared/model/temperature.model';
import { TemperatureService } from 'app/entities/temperature/temperature.service';

@Component({
  selector: 'jhi-value-update',
  templateUrl: './value-update.component.html',
})
export class ValueUpdateComponent implements OnInit {
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
    protected valueService: ValueService,
    protected temperatureService: TemperatureService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ value }) => {
      if (!value.id) {
        const today = moment().startOf('day');
        value.timestamp = today;
      }

      this.updateForm(value);

      this.temperatureService.query().subscribe((res: HttpResponse<ITemperature[]>) => (this.temperatures = res.body || []));
    });
  }

  updateForm(value: IValue): void {
    this.editForm.patchValue({
      id: value.id,
      value: value.value,
      timestamp: value.timestamp ? value.timestamp.format(DATE_TIME_FORMAT) : null,
      temperature: value.temperature,
      temperatures: value.temperatures,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const value = this.createFromForm();
    if (value.id !== undefined) {
      this.subscribeToSaveResponse(this.valueService.update(value));
    } else {
      this.subscribeToSaveResponse(this.valueService.create(value));
    }
  }

  private createFromForm(): IValue {
    return {
      ...new Value(),
      id: this.editForm.get(['id'])!.value,
      value: this.editForm.get(['value'])!.value,
      timestamp: this.editForm.get(['timestamp'])!.value ? moment(this.editForm.get(['timestamp'])!.value, DATE_TIME_FORMAT) : undefined,
      temperature: this.editForm.get(['temperature'])!.value,
      temperatures: this.editForm.get(['temperatures'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IValue>>): void {
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
