import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ValueDetailComponent } from 'app/entities/value/value-detail.component';
import { Value } from 'app/shared/model/value.model';

describe('Component Tests', () => {
  describe('Value Management Detail Component', () => {
    let comp: ValueDetailComponent;
    let fixture: ComponentFixture<ValueDetailComponent>;
    const route = ({ data: of({ value: new Value(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ValueDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ValueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ValueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load value on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.value).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
