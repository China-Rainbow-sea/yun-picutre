import { defineStore } from "pinia";
import { ref } from "vue";


/**
 * 存储登录用户信息的状态
 */
export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<any>({
    userName: "未登录",
  });


  async function fetchLoginUser() {
    // todo 由于后端还没提供接口，暂时注释
    // const res = await getCurrentUser();
    // if (res.data.code === 0 && res.data.data) {
    //   loginUser.value = res.data.data;
    // }
    setTimeout(()=>{
      loginUser.value = {userName:'测试用户',id: 1}
    },3000)
  }

  /**
   * 设置登录用户
   */
  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser;
  }

  // 返回
  return { loginUser, setLoginUser, fetchLoginUser };
});

