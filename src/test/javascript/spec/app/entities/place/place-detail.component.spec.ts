import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { PlaceDetailComponent } from 'app/entities/place/place-detail.component';
import { Place } from 'app/shared/model/place.model';

describe('Component Tests', () => {
  describe('Place Management Detail Component', () => {
    let comp: PlaceDetailComponent;
    let fixture: ComponentFixture<PlaceDetailComponent>;
    const route = ({ data: of({ place: new Place(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [PlaceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PlaceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlaceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load place on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.place).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
