import Vue from 'vue';
import App from './App.vue';
import router from './router';

import { Button } from 'element-ui';

Vue.config.productionTip = true;

Vue.component(Button.name, Button);

new Vue({
  router,
  render: h => h(App)
}).$mount('#app');
