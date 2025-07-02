<template>
  <a-row :wrap="false">
    <a-col flex="250px">
      <RouterLink to="/">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.jpg" alt="logo" />
          <div class="title">智能协同云图库</div>
        </div>
      </RouterLink>
    </a-col>
    <a-col flex="auto">
      <a-menu v-model:selectedKeys="current" mode="horizontal" :items="items" @click="doMenuClick" />
    </a-col>
    <a-col flex="120px">
      <div class="user-login-status">
        <div v-if="loginUserStore.loginUser.id">
          {{ loginUserStore.loginUser.userName ?? '无名' }}
        </div>
        <div v-else>
          <a-button type="primary" href="/user/login">登录</a-button>
        </div>
      </div>

    </a-col>
  </a-row>

</template>

<script lang="ts" setup>
import { h, ref } from 'vue'
import { HomeOutlined } from '@ant-design/icons-vue'
import { type MenuProps } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
const loginUserStore = useLoginUserStore()
loginUserStore.fetchLoginUser()

const router = useRouter()
// 路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  });
};


// 当前选中菜单项，同时也是要高亮的菜单项
const current = ref<string[]>([])
// 路由跳转事件
router.afterEach((to, from) => {
  // 路径上的url地址
  current.value = [to.path]
})


const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页'
  },
  {
    key: '/about',
    label: '关于',
    title: '关于'
  },
  {
    key: 'others',
    label: h(
      'a',
      { href: 'https://rainbowsea.blog.csdn.net', target: '_blank' },
      'RainbowSea 博客'
    ),
    title: 'RainbowSea 博客地址'
  }
])
</script>


<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}

.logo {
  height: 48px;
}
</style>
