import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ValuesDetailComponent } from 'app/entities/values/values-detail.component';
import { Values } from 'app/shared/model/values.model';

describe('Component Tests', () => {
  describe('Values Management Detail Component', () => {
    let comp: ValuesDetailComponent;
    let fixture: ComponentFixture<ValuesDetailComponent>;
    const route = ({ data: of({ values: new Values(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ValuesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ValuesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ValuesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load values on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.values).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
