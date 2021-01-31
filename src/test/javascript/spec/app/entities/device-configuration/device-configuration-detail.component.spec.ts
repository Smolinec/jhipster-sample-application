import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { DeviceConfigurationDetailComponent } from 'app/entities/device-configuration/device-configuration-detail.component';
import { DeviceConfiguration } from 'app/shared/model/device-configuration.model';

describe('Component Tests', () => {
  describe('DeviceConfiguration Management Detail Component', () => {
    let comp: DeviceConfigurationDetailComponent;
    let fixture: ComponentFixture<DeviceConfigurationDetailComponent>;
    const route = ({ data: of({ deviceConfiguration: new DeviceConfiguration(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [DeviceConfigurationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DeviceConfigurationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeviceConfigurationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load deviceConfiguration on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.deviceConfiguration).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
