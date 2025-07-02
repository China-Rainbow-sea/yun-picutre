<template>
  <div id="basicLayout">
    <a-layout>
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <HomePage/>
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="https://rainbowsea.blog.csdn.net/" target="_blank"> RainbowSea 博客</a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import { healthUsingGet } from '@/api/mainController'
import HomePage from '@/pages‎/HomePage.vue'

healthUsingGet().then((res) => {
  console.log(res)
})


</script>


<style scoped>

#basicLayout .header {
  padding-inline: 20px;
  margin-bottom: 16px;
  color: unset;
  background: white;
}

#basicLayout .content{
  padding: 20px;
  margin-bottom: 28px;
  background: linear-gradient(to right,#fefefe,#fff);
}

#basicLayout .footer {
  background: #efefef;
  padding: 16px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}




</style>

