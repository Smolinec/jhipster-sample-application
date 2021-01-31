import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { DeviceProfileDetailComponent } from 'app/entities/device-profile/device-profile-detail.component';
import { DeviceProfile } from 'app/shared/model/device-profile.model';

describe('Component Tests', () => {
  describe('DeviceProfile Management Detail Component', () => {
    let comp: DeviceProfileDetailComponent;
    let fixture: ComponentFixture<DeviceProfileDetailComponent>;
    const route = ({ data: of({ deviceProfile: new DeviceProfile(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [DeviceProfileDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DeviceProfileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeviceProfileDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load deviceProfile on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.deviceProfile).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
