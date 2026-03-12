import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/job-request">
        <Translate contentKey="global.menu.entities.jobRequest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/job-report">
        <Translate contentKey="global.menu.entities.jobReport" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/job-execution-report">
        <Translate contentKey="global.menu.entities.jobExecutionReport" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sanitization-report">
        <Translate contentKey="global.menu.entities.sanitizationReport" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
