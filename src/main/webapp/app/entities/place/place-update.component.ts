import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPlace, Place } from 'app/shared/model/place.model';
import { PlaceService } from './place.service';
import { IWebUser } from 'app/shared/model/web-user.model';
import { WebUserService } from 'app/entities/web-user/web-user.service';

@Component({
  selector: 'jhi-place-update',
  templateUrl: './place-update.component.html',
})
export class PlaceUpdateComponent implements OnInit {
  isSaving = false;
  webusers: IWebUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    webUsers: [],
  });

  constructor(
    protected placeService: PlaceService,
    protected webUserService: WebUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ place }) => {
      this.updateForm(place);

      this.webUserService.query().subscribe((res: HttpResponse<IWebUser[]>) => (this.webusers = res.body || []));
    });
  }

  updateForm(place: IPlace): void {
    this.editForm.patchValue({
      id: place.id,
      name: place.name,
      webUsers: place.webUsers,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const place = this.createFromForm();
    if (place.id !== undefined) {
      this.subscribeToSaveResponse(this.placeService.update(place));
    } else {
      this.subscribeToSaveResponse(this.placeService.create(place));
    }
  }

  private createFromForm(): IPlace {
    return {
      ...new Place(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      webUsers: this.editForm.get(['webUsers'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlace>>): void {
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

  trackById(index: number, item: IWebUser): any {
    return item.id;
  }

  getSelected(selectedVals: IWebUser[], option: IWebUser): IWebUser {
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
