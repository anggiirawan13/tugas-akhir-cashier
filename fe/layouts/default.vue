<template>
  <v-app>
    <v-navigation-drawer disable-resize-watcher v-model="sideDrawer" fixed app>
      <v-list>
        <v-list-item
          v-for="(item, i) in sideMenu"
          :key="i"
          :to="item.to"
          router
          exact
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <v-container fill-height fluid>
        <Nuxt />
      </v-container>
    </v-main>

    <v-bottom-navigation horizontal height="10vh" fixed color="primary" app>
      <v-app-bar-nav-icon
        @click.stop="sideDrawer = !sideDrawer"
        :ripple="false"
      />
      <v-spacer />
    </v-bottom-navigation>
  </v-app>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "DefaultLayout",
  data() {
    return {
      sideDrawer: false,
      sideMenu: [],
      originalSideMenu: [
        {
          icon: "mdi-view-dashboard-variant",
          title: "Dashboard",
          to: "/dashboard",
          middlewares: ["authenticated"],
        },
        {
          icon: "mdi-application",
          title: "Cashier App",
          to: "/",
          middlewares: ["cashier"],
        },
        {
          icon: "mdi-coffee",
          title: "Product Management",
          to: "/product",
          middlewares: ["admin"],
        },
        {
          icon: "mdi-shape",
          title: "Category Management",
          to: "/category",
          middlewares: ["admin"],
        },
        {
          icon: "mdi-account",
          title: "User Management",
          to: "/user",
          middlewares: ["admin"],
        },
        {
          icon: "mdi-login",
          title: "Login",
          to: "/login",
          middlewares: ["unauthenticated"],
        },
        {
          icon: "mdi-logout",
          title: "Logout",
          to: "/logout",
          middlewares: ["authenticated"],
        },
      ],
      bottomMenu: [],
      originalBottomMenu: [
        {
          icon: "mdi-credit-card-check-outline",
          title: "Check Out",
          middlewares: ["authenticated"],
        },
      ],
    };
  },
  computed: {
    ...mapGetters("auth", {
      authenticated: ["authenticated"],
      user: ["user"],
    }),
  },
  methods: {
    isWelcomeScreen() {
      if (
        localStorage.welcomeScreen === "false" &&
        this.$router.currentRoute.path !== "/register" &&
        this.$router.currentRoute.path !== "/login"
      ) {
        this.$router.push("/register");
      }
    },
    filterMenu() {
      this.sideMenu = this.originalSideMenu.filter((item) => {
        if (this.authenticated) {
          return (
            item.middlewares.includes("authenticated") ||
            item.middlewares.includes(this.user.role)
          );
        } else {
          return item.middlewares.includes("unauthenticated");
        }
      });

      this.bottomMenu = this.originalBottomMenu.filter((item) => {
        if (this.authenticated) {
          return item.middlewares.includes("authenticated");
        }
      });
    },
  },
  watch: {
    $route() {
      this.isWelcomeScreen();
    },
    authenticated() {
      this.filterMenu();
    },
  },
  mounted() {
    if (this.$router.currentRoute.path === "/") {
      this.isWelcomeScreen();
    }

    this.filterMenu();
  },
};
</script>
