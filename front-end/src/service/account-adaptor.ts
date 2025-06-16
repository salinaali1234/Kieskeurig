import { Account } from "@/models/Account.ts";

const BASE_URL = import.meta.env.VITE_APP_BACKEND_URL;

export class AccountsAdaptor {
  private readonly resourcesUrl: string;

  constructor() {
    this.resourcesUrl = `${BASE_URL}/accounts`;
  }

  private async fetchJson(url: string, options?: RequestInit): Promise<any> {
    const response = await fetch(url, options);
    if (!response.ok) {
      throw new Error(await response.text());
    }
    return await response.json();
  }

  async getAll(): Promise<Account[]> {
    const data = await this.fetchJson(this.resourcesUrl);
    return data.map((d: any) => Account.copyConstructor(d));
  }

  async getById(id: number): Promise<Account | null> {
    const data = await this.fetchJson(`${this.resourcesUrl}/${id}`);
    return Account.copyConstructor(data);
  }

  async save(account: Account): Promise<Account | null> {
    const method = account.id ? 'PUT' : 'POST';
    const url = account.id ? `${this.resourcesUrl}/${account.id}` : this.resourcesUrl;

    const data = await this.fetchJson(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(account)
    });

    return Account.copyConstructor(data);
  }

  async delete(id: number): Promise<void> {
    await this.fetchJson(`${this.resourcesUrl}/${id}`, {
      method: 'DELETE'
    });
  }
}
