import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValue } from 'app/shared/model/value.model';

@Component({
  selector: 'jhi-value-detail',
  templateUrl: './value-detail.component.html',
})
export class ValueDetailComponent implements OnInit {
  value: IValue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ value }) => (this.value = value));
  }

  previousState(): void {
    window.history.back();
  }
}
