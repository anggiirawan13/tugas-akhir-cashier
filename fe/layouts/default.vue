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
          middleware: ["authenticated"],
        },
        {
          icon: "mdi-application",
          title: "Cashier App",
          to: "/",
          middleware: ["cashier"],
        },
        {
          icon: "mdi-coffee",
          title: "Product Management",
          to: "/product",
          middleware: ["admin"],
        },
        {
          icon: "mdi-shape",
          title: "Category Management",
          to: "/category",
          middleware: ["admin"],
        },
        {
          icon: "mdi-account",
          title: "User Management",
          to: "/user",
          middleware: ["admin"],
        },
        {
          icon: "mdi-view-dashboard-variant",
          title: "Report",
          to: "/report",
          middleware: ["admin"],
        },
        {
          icon: "mdi-login",
          title: "Login",
          to: "/login",
          middleware: ["unauthenticated"],
        },
        {
          icon: "mdi-logout",
          title: "Logout",
          to: "/logout",
          middleware: ["authenticated"],
        },
      ],
      bottomMenu: [],
      originalBottomMenu: [
        {
          icon: "mdi-credit-card-check-outline",
          title: "Check Out",
          middleware: ["cashier"],
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
              item.middleware.includes("authenticated") ||
              item.middleware.includes(this.user.role)
          );
        } else {
          return item.middleware.includes("unauthenticated");
        }
      });

      this.bottomMenu = this.originalBottomMenu.filter((item) => {
        if (this.authenticated) {
          return item.middleware.includes("authenticated");
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
