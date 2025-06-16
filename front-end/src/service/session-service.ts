import { Account } from "@/models/Account";

export class SessionService {
  private readonly RESOURCES_URL: string;
  private readonly STORAGE_KEY: string;
  private _currentToken: string | null = null;
  private _currentAccount: Account | null = null;

  constructor(resourcesUrl: string, storageKey: string) {
    this.RESOURCES_URL = resourcesUrl;
    this.STORAGE_KEY = storageKey;
    this.loadFromStorage();
  }

  get currentToken(): string | null {
    return this._currentToken;
  }

  get currentAccount(): Account | null {
    return this._currentAccount;
  }

  isAdmin(): boolean {
    return this._currentAccount?.role?.toLowerCase().includes("admin") ?? false;
  }

  isAuthenticated(): boolean {
    return this._currentAccount !== null;
  }

  private loadFromStorage(): void {
    this._currentToken = sessionStorage.getItem(`${this.STORAGE_KEY}_token`);
    const accountData = sessionStorage.getItem(`${this.STORAGE_KEY}_account`);
    this._currentAccount = accountData ? Account.copyConstructor(JSON.parse(accountData)) : null;
  }

  saveSession(token: string | null, account: Account | null): void {
    this._currentToken = token;
    this._currentAccount = account;

    if (token && account) {
      sessionStorage.setItem(`${this.STORAGE_KEY}_token`, token);
      sessionStorage.setItem(`${this.STORAGE_KEY}_account`, JSON.stringify(account));
    } else {
      sessionStorage.removeItem(`${this.STORAGE_KEY}_token`);
      sessionStorage.removeItem(`${this.STORAGE_KEY}_account`);
    }
  }
  async login(email: string, password: string): Promise<Account | null> {
    try {
      const response = await fetch(`${this.RESOURCES_URL}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify({
          email: email.trim().toLowerCase(),
          password: password
        }),
        credentials: 'include'
      });

      const contentType = response.headers.get("Content-Type") || "";

      if (!response.ok) {
        const errorMsg = contentType.includes("application/json")
          ? (await response.json()).message || 'Login failed'
          : await response.text();

        throw new Error(errorMsg);
      }

      if (!contentType.includes("application/json")) {
        throw new Error("Expected JSON response");
      }

      const body = await response.json();

      const token = response.headers.get('Authorization');
      if (!token) throw new Error('Authentication token missing');

      const { account } = body;
      if (!account || !account.id) throw new Error('Invalid account data received');

      this.saveSession(token, account);
      return Account.copyConstructor(account);
    } catch (e: never) {
      console.error('Login error:', e);
      throw new Error(e.message || 'Login failed. Please try again.');
    }
  }

  logout(): void {
    this.saveSession(null, null);
  }
}
