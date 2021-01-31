import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValues } from 'app/shared/model/values.model';

@Component({
  selector: 'jhi-values-detail',
  templateUrl: './values-detail.component.html',
})
export class ValuesDetailComponent implements OnInit {
  values: IValues | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ values }) => (this.values = values));
  }

  previousState(): void {
    window.history.back();
  }
}
